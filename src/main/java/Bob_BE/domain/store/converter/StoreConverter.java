package Bob_BE.domain.store.converter;

import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.menu.dto.response.MenuResponseDto.SearchMenuResponseDto;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.signatureMenu.entity.SignatureMenu;
import Bob_BE.domain.store.dto.response.StoreResponseDto.GetStoreSearchDto;
import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.university.entity.University;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StoreConverter {

    public static StoreResponseDto.CertificateResultDto toCertificateResultDto(List<String> datas) {

        String businessType = datas.get(4);
        businessType = businessType.replace(" ,", ",");

        String categories = datas.get(5);
        categories = categories.replace(" ,", ",");

        return StoreResponseDto.CertificateResultDto.builder()
                .registrationNumber(datas.get(1))
                .storeName(datas.get(2))
                .address(datas.get(3))
                .businessTypes(businessType)
                .categories(categories)
                .build();
    }

    public static StoreResponseDto.GetDataForPingResponseDto toGetDataForPingResponseDto (List<StoreResponseDto.StoreDataDto> storeDataDtoList) {

        return StoreResponseDto.GetDataForPingResponseDto.builder()
                .storeDataDtoList(storeDataDtoList)
                .totalDataNum(storeDataDtoList.size())
                .build();
    }

    public static StoreResponseDto.StoreDataDto toStoreDataDto (Store store, Menu menu, int discountPrice) {

        return StoreResponseDto.StoreDataDto.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .menuPrice(menu.getPrice())
                .discountPrice(discountPrice)
                .build();
    }

    public static StoreResponseDto.GetStoreDataResponseDto toGetStoreDataResponseDto(Store store, List<StoreResponseDto.StoreMenuData> storeMenuDataList) {

        String bannerUrl;
        if (store.getBanner() != null) {

            bannerUrl = store.getBanner().getBannerUrl();
        }
        else {

            bannerUrl = "https://bab-e-deuk-bucket.s3.ap-northeast-2.amazonaws.com/Default/default_banner.png";
        }

        Boolean onSale = store.getDiscountList().stream()
                .anyMatch(Discount::getInProgress);

        Discount discount = new Discount();

        if (onSale) {
            discount = store.getDiscountList().stream()
                    .filter(Discount::getInProgress)
                    .collect(Collectors.toList()).get(0);


        }

        StoreResponseDto.StoreDiscountData storeDiscountData = StoreResponseDto.StoreDiscountData.builder()
                .discountId(discount.getId())
                .title(discount.getTitle())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .build();

        SignatureMenu signatureMenu = new SignatureMenu();
        if (store.getSignatureMenu() != null) signatureMenu = store.getSignatureMenu();

        return StoreResponseDto.GetStoreDataResponseDto.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .onSale(onSale)
                .storeLink(store.getStoreLink())
                .signatureMenuId(signatureMenu.getMenu().getId())
                .bannerUrl(bannerUrl)
                .storeDiscountData(storeDiscountData)
                .storeMenuDataList(storeMenuDataList)
                .build();
    }

    public static StoreResponseDto.StoreMenuData toGetStoreMenuDataDto(Menu menu, int discountPrice, int discountRate) {

        return StoreResponseDto.StoreMenuData.builder()
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
                .toList();
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
                .toList();
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
                }).toList();

        return StoreResponseDto.GetMenuNameListResponseDto.builder()
                .menuNameDataDtoList(menuNameDataDtoList)
                .totalDataNum(menuNameDataDtoList.size())
                .build();
    }


    public static StoreResponseDto.StoreCreateResultDto toCreateStoreResponseDto(Store store){
        String bannerImageUrl = store.getBanner() != null ? store.getBanner().getBannerUrl() : null;

        return StoreResponseDto.StoreCreateResultDto.builder()
                .id(store.getId())
                .name(store.getName())
                .bannerImageUrl(bannerImageUrl)
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

    public static GetStoreSearchDto toStoreSearchResponseDto(Store store, String keyword, Double distance){
        List<SearchMenuResponseDto> menus = store.getMenuList().stream()
                .filter(menu -> menu.getMenuName().contains(keyword))
                .map(StoreConverter::toMenuResponseDto)
                .collect(Collectors.toCollection(ArrayList::new));


        return GetStoreSearchDto.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .distanceFromUniversityKm(distance)
                .menuList(menus)
                .build();
    }

    private static SearchMenuResponseDto toMenuResponseDto(Menu menu){
        Integer discountPrice = menu.getDiscountMenuList().stream()
                .filter(discountMenu -> discountMenu.getDiscount().getInProgress())
                .map(DiscountMenu::getDiscountPrice)
                .findFirst()
                .orElse(null);

        return SearchMenuResponseDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuImageUrl(menu.getMenuUrl())
                .isSignature(menu.getSignatureMenu() != null)
                .discountPrice(discountPrice)
                .build();
    }

    public static StoreResponseDto.StoreInformDto toStoreInformDto(Store store, String bannerUrl, University university){
        return StoreResponseDto.StoreInformDto.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .bannerImageUrl(bannerUrl)
                .storeUniversity(university.getUniversityName())
                .storeAddress(store.getStreetAddress())
                .build();
    }
}
