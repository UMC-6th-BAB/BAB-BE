package Bob_BE.domain.store.converter;


import Bob_BE.domain.banner.entity.Banner;
import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.menu.entity.Menu;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.Store;

public class StoreConverter {

    public static StoreResponseDto.GetStoreDataResponseDto toGetStoreDataResponseDto(Store store, List<StoreResponseDto.GetStoreMenuDataDto> getStoreMenuDataDtoList) {

        List<String> bannerUrlList = store.getBannerList().stream()
                .map(Banner::getBannerUrl)
                .collect(Collectors.toList());

        return StoreResponseDto.GetStoreDataResponseDto.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .storeLink(store.getStoreLink())
                .signatureMenuId(store.getSignatureMenu().getMenu().getId())
                .bannerUrlList(bannerUrlList)
                .getStoreMenuDataDtoList(getStoreMenuDataDtoList)
                .build();
    }

    public static StoreResponseDto.GetStoreMenuDataDto toGetStoreMenuDataDto(Menu menu, int discountPrice, int discountRate) {

        return StoreResponseDto.GetStoreMenuDataDto.builder()
                .menuId(menu.getId())
                .menuName(menu.getMenuName())
                .menuUrl(menu.getMenuUrl())
                .menuPrice(menu.getPrice())
                .discountPrice(discountPrice)
                .discountRate(discountRate)
                .build();
    }

    public static List<StoreResponseDto.GetOnSaleStoreDataDto> toGetOnSaleStoreDataDtoList (List<StoreResponseDto.StoreAndDiscountDataDto> storeAndDiscountDataDtoList) {

        return storeAndDiscountDataDtoList.stream()
                .map(storeAndDiscountDataDto -> {
                    return StoreResponseDto.GetOnSaleStoreDataDto.builder()
                            .storeId(storeAndDiscountDataDto.getStore().getId())
                            .storeName(storeAndDiscountDataDto.getStore().getName())
                            .discountTitle(storeAndDiscountDataDto.getDiscount().getTitle())
                            .discountId(storeAndDiscountDataDto.getDiscount().getId())
                            .getOnSaleStoreMenuDataDtoList(new ArrayList<>())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public static List<StoreResponseDto.GetOnSaleStoreInMyPageDto> toGetOnSaleStoreInMyPageDtoList (List<StoreResponseDto.StoreAndDiscountDataDto> storeAndDiscountDataDtoList) {

        return storeAndDiscountDataDtoList.stream()
                .limit(3)
                .map(storeAndDiscountDataDto -> {
                    return StoreResponseDto.GetOnSaleStoreInMyPageDto.builder()
                            .storeId(storeAndDiscountDataDto.getStore().getId())
                            .storeName(storeAndDiscountDataDto.getStore().getName())
                            .discountTitle(storeAndDiscountDataDto.getDiscount().getTitle())
                            .discountId(storeAndDiscountDataDto.getDiscount().getId())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public static StoreResponseDto.GetOnSaleStoreListResponseDto toGetOnSaleStoreListResponseDto (List<StoreResponseDto.GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList) {

        return StoreResponseDto.GetOnSaleStoreListResponseDto.builder()
                .getOnSaleStoreDataDtoList(getOnSaleStoreDataDtoList)
                .totalDateNum(getOnSaleStoreDataDtoList.size())
                .build();
    }

    public static StoreResponseDto.GetMenuNameListResponseDto toGetMenuNameListResponseDto (List<Menu> menuList) {

        List<StoreResponseDto.MenuNameDataDto> menuNameDataDtoList = menuList.stream()
                .map(menu -> {
                    return StoreResponseDto.MenuNameDataDto.builder()
                            .menuId(menu.getId())
                            .name(menu.getMenuName())
                            .build();
                }).collect(Collectors.toList());

        return StoreResponseDto.GetMenuNameListResponseDto.builder()
                .menuNameDataDtoList(menuNameDataDtoList)
                .totalDataNum(menuNameDataDtoList.size())
                .build();
    }


    public static StoreResponseDto.StoreCreateResultDto toCreateStoreResponseDto(Store store){

        return StoreResponseDto.StoreCreateResultDto.builder()
                .id(store.getId())
                .name(store.getName())
                .build();
    }

    public static StoreResponseDto.StoreDeleteResultDto toDeleteStoreResponseDto(Store store){

        return StoreResponseDto.StoreDeleteResultDto.builder()
                .id(store.getId())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static Store toStore(Owner owner, StoreRequestDto.StoreCreateRequestDto requestDto){

        return Store.builder()
                .owner(owner)
                .longitude(requestDto.getLongitude())
                .latitude(requestDto.getLatitude())
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .streetAddress(requestDto.getStreetAddress())
                .storeLink(requestDto.getStoreLink())
                .registration(requestDto.getRegistration())
                .build();
    }
}
