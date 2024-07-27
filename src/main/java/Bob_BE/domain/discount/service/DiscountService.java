package Bob_BE.domain.discount.service;

import Bob_BE.domain.discount.converter.DiscountConverter;
import Bob_BE.domain.discount.dto.parameter.DiscountParameterDto;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.discount.repository.DiscountRepository;
import Bob_BE.domain.discountMenu.converter.DiscountMenuConverter;
import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.DiscountHandler;
import Bob_BE.global.response.exception.handler.MenuHandler;
import Bob_BE.global.response.exception.handler.StoreHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    /**
     * 할인 추가 API
     * return : Discount
     */
    @Transactional
    public Discount CreateDiscount(@Valid DiscountParameterDto.CreateDiscountParamDto param) {

        Store findStore = storeRepository.findById(param.getStoreId())
                .orElseThrow(() -> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        Discount newDiscount = DiscountConverter.toDiscount(param, findStore);
        newDiscount.setInProgress(); // inProgress 값 설정

        // DiscountMenu 설정
        List<DiscountMenu> discountMenuList = param.getDiscountMenuDataDtoList().stream()
                .map(discountMenuDataDto -> {
                    Menu findMenu = menuRepository.findById(discountMenuDataDto.getMenuId())
                            .orElseThrow(() -> new MenuHandler(ErrorStatus.MENU_NOT_FOUND));
                    DiscountMenu discountMenu = DiscountMenuConverter.toDiscountMenu(findMenu, discountMenuDataDto.getDiscountPrice());
                    discountMenu.setMenu(findMenu);
                    return discountMenu;
                }).collect(Collectors.toList());

        discountMenuList.stream()
                .forEach(discountMenu -> discountMenu.setDiscount(newDiscount));

        Discount savedDiscount = discountRepository.save(newDiscount);

        return savedDiscount;
    }

    /**
     * 할인 삭제
     * return : void
     */
    @Transactional
    public void DeleteDiscount(@Valid DiscountParameterDto.DeleteDiscountParamDto param) {

        Store findStore = storeRepository.findById(param.getStoreId())
                .orElseThrow(() -> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        Discount findDiscount = discountRepository.findById(param.getDiscountId())
                .orElseThrow(() -> new DiscountHandler(ErrorStatus.DISCOUNT_NOT_FOUND));

        if (!findDiscount.getStore().equals(findStore)) throw new DiscountHandler(ErrorStatus.DISCOUNT_STORE_NOT_MATCH);

        discountRepository.delete(findDiscount);
    }

    /**
     * 할인 행사 상태 변경
     * 매일 자정에 실행
     */
    @Transactional
    @Scheduled(cron = "1 0 0 * * *")
    public void ChangeDiscountProgress() {

        List<Discount> discountList = discountRepository.findAll();
        discountList.stream()
                .forEach(Discount::setInProgress);
    }

    /**
     * 진행했던 할인 행사 목록 가져오기
     * return : List<Discount>
     */
    public List<Discount> GetDiscountedList(@Valid DiscountParameterDto.GetDiscountedListParamDto param) {

        Store findStore = storeRepository.findById(param.getStoreId())
                .orElseThrow(() -> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        List<Discount> discountedList = discountRepository.findAllByStoreAndInProgress(findStore, false)
                .orElse(new ArrayList<>());

        discountedList.removeIf(discount -> discount.getStartDate().compareTo(LocalDate.now()) > 0);

        return discountedList;
    }
}
