package Bob_BE.domain.menu.service;

import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.menu.converter.MenuConverter;
import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuDeleteRequestDto;
import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuUpdateRequestDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto.DeleteMenuResponseDto;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.repository.MenuRepository;
import Bob_BE.domain.store.converter.StoreConverter;
import Bob_BE.domain.store.dto.parameter.StoreParameterDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.store.repository.StoreRepository;
import Bob_BE.global.response.code.resultCode.ErrorStatus;
import Bob_BE.global.response.exception.handler.MenuHandler;

import Bob_BE.global.util.aws.S3StorageService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import Bob_BE.global.response.exception.handler.StoreHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final S3StorageService s3StorageService;

    @Transactional
    public MenuResponseDto.CreateMenuResponseDto updateMenu(Long menuId, MenuUpdateRequestDto requestDTO) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuHandler(ErrorStatus.MENU_NOT_FOUND));
        menu.setMenuName(requestDTO.getMenuName());
        menu.setPrice(requestDTO.getPrice());
        menu.setMenuUrl(requestDTO.getMenuUrl());
        menuRepository.save(menu);

        return MenuConverter.toCreateMenuResponseDto(menu);
    }

    @Transactional
    public List<DeleteMenuResponseDto> deleteMenus(MenuDeleteRequestDto requestDTO) {
        List<Long> menuIds = requestDTO.getMenuIds();
        return menuIds.stream().map(menuId -> {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new MenuHandler(ErrorStatus.MENU_NOT_FOUND));
            menuRepository.delete(menu);
            return MenuConverter.toDeleteMenuResponseDto(menuId);
        }).toList();
    }

    @Transactional
    public String uploadMenuImage(MultipartFile imageFile) {
        String imageUrl;

        try {
            imageUrl = s3StorageService.uploadFile(imageFile, "Menu");
        } catch (Exception e) {
            throw new MenuHandler(ErrorStatus.FILE_UPLOAD_FAILED);
        }

        return imageUrl;
    }

    public List<Menu> GetMenuListByStore (@Valid StoreParameterDto.GetMenuNameListParamDto param) {

        Store findStore = storeRepository.findById(param.getStoreId())
                .orElseThrow(() -> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        List<Menu> menuList = menuRepository.findAllByStore(findStore)
                .orElse(new ArrayList<>());

        return menuList;
    }

    /**
     * 가게 상세 페이지 API : 메뉴 데이터 가져오기
     * return : List<GetStoreMenuDataDto>
     */
    public List<StoreResponseDto.GetStoreMenuDataDto> GetStoreMenuData(StoreParameterDto.GetStoreDataParamDto param) {

        Store findStore = storeRepository.findById(param.getStoreId())
                .orElseThrow(() -> new StoreHandler(ErrorStatus.STORE_NOT_FOUND));

        List<Menu> menuList = menuRepository.findAllByStore(findStore)
                .orElse(new ArrayList<>());

        return menuList.stream()
                .map(menu -> {

                    AtomicInteger discountPrice = new AtomicInteger(0);
                    AtomicInteger discountRate = new AtomicInteger(0);

                    for (DiscountMenu discountMenu : menu.getDiscountMenuList()) {
                        discountPrice.addAndGet(discountMenu.getDiscountPrice());
                    }

                    if (discountPrice.get() != 0) {
                        discountRate.set((int)((discountPrice.get() / (double)menu.getPrice()) * 100));
                    }

                    System.out.println("discountRate : " + discountRate);
                    return StoreConverter.toGetStoreMenuDataDto(menu, discountPrice.get(), discountRate.get());
                }).collect(Collectors.toList());
    }
}
