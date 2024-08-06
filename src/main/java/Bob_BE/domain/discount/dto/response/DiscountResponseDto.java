package Bob_BE.domain.discount.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DiscountResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiscountResponseDto {

        private Long discountId;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteDiscountResponseDto {

        private String message;
        private LocalDateTime deletedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetDiscountedListResponseDto {

        private List<GetDiscountDataDto> getDiscountDataDtoList;
        private Integer totalDataNum;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetDiscountDataDto {

        private Long discountId;
        private String storeName;
        private String discountTitle;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
