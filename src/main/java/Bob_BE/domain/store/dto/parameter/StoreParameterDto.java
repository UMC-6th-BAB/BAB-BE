package Bob_BE.domain.store.dto.parameter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StoreParameterDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMenuNameListParamDto {

        private Long storeId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetOnSaleStoreListParamDto {

        private String authorizationHeader;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetDataForPingParamDto {

        private String authorizationHeader;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetStoreDataParamDto {

        private Long storeId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetSearchKeywordParamDto {
        private String authorizationHeader;
        private String keyword;
        private Double latitude;
        private Double longitude;
    }
}
