package Bob_BE.domain.store.converter;


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
