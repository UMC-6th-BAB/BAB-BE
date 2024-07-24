package Bob_BE.domain.discount.service;

import Bob_BE.domain.DiscountMenu.converter.DiscountMenuConverter;
import Bob_BE.domain.DiscountMenu.entity.DiscountMenu;
import Bob_BE.domain.discount.controller.DiscountController;
import Bob_BE.domain.discount.converter.DiscountConverter;
import Bob_BE.domain.discount.dto.parameter.DiscountParameterDto;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.discount.repository.DiscountRepository;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.GeneralException;
import Bob_BE.global.response.exception.handler.DiscountHandler;
import Bob_BE.global.response.exception.handler.MenuHandler;
import Bob_BE.global.response.exception.handler.StoreHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    /**
     * 할인 추가 API
     * return : Discount
     */
    @Override
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
}
