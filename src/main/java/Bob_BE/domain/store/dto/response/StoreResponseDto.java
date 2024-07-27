package Bob_BE.domain.store.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public static class GetOnSaleStoreDataDto {

        private final Long storeId;
        private final String discountTitle;
        private final String storeName;
        private final List<GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList;

        @QueryProjection
        public GetOnSaleStoreDataDto(Long storeId, String discountTitle, String storeName, List<GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList) {

            this.storeId = storeId;
            this.discountTitle = discountTitle;
            this.storeName = storeName;
            this.getOnSaleStoreDataDtoList = getOnSaleStoreDataDtoList;
        }
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
}
