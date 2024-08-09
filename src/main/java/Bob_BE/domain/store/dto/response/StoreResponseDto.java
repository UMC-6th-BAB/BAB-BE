package Bob_BE.domain.store.dto.response;

import Bob_BE.domain.banner.entity.Banner;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.store.entity.Store;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class StoreResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MenuCreateResultDto {
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMenuNameListResponseDto {

        private List<MenuNameDataDto> menuNameDataDtoList;
        private Integer totalDataNum;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuNameDataDto {

        @NotNull
        private Long menuId;
        @NotNull
        private String name;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreCreateResultDto {
        private Long id;
        private String name;
        private List<String> bannerImageUrls;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreUpdateResultDto {
        private Long id;
        private String name;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetOnSaleStoreListResponseDto {

        private List<GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList;
        private Integer totalDateNum;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreDeleteResultDto {
        private Long id;
        private LocalDateTime deletedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetOnSaleStoreDataDto {

        private Long storeId;
        private String storeName;
        private Long discountId;
        private String discountTitle;

        @Setter
        private List<GetOnSaleStoreMenuDataDto> getOnSaleStoreMenuDataDtoList;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetOnSaleStoreInMyPageDto {

        private Long storeId;
        private String storeName;
        private Long discountId;
        private String discountTitle;
    }

    @Getter
    public static class GetOnSaleStoreMenuDataDto {

        private final String menuName;
        private final Integer price;
        private final Integer discountPrice;

        @QueryProjection
        public GetOnSaleStoreMenuDataDto(String menuName, Integer price, Integer discountPrice) {
            this.menuName = menuName;
            this.price = price;
            this.discountPrice = discountPrice;
        }
    }

    @Getter
    public static class StoreAndDiscountDataDto {

        private final Store store;
        private final Discount discount;

        @QueryProjection
        public StoreAndDiscountDataDto(Store store, Discount discount) {

            this.store = store;
            this.discount = discount;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetDataForPingResponseDto {

        private List<StoreDataDto> storeDataDtoList;
        private Integer totalDataNum;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreDataDto {

        private Long storeId;
        private String storeName;
        private Double latitude;
        private Double longitude;
        private Integer menuPrice;
        private Integer discountPrice;
      
    }
  
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetStoreDataResponseDto {

        private Long storeId;
        private String storeName;
        private String storeLink;
        private Boolean onSale;
        private Long signatureMenuId;
        private List<String> bannerUrlList;
        private List<GetStoreMenuDataDto> getStoreMenuDataDtoList;
    }

    @Getter 
    @Builder 
    @NoArgsConstructor 
    @AllArgsConstructor
    public static class GetStoreMenuDataDto {

        private Long menuId;
        private String menuName;
        private String menuUrl;
        private Integer menuPrice;
        private Integer discountPrice;
        private Integer discountRate;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CertificateResultDto {
        private String registrationNumber;
        private String storeName;
        private String address;
        private String businessTypes;
        private String categories;
    }

}
