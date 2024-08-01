package Bob_BE.domain.operatingHours.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OHResponseDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class OHCreateResultDto{

        private Long storeId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class OHUpdateResultDto{

        private Long storeId;
    }
}
