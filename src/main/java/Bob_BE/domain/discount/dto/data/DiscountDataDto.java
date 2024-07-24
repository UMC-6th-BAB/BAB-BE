package Bob_BE.domain.discount.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class DiscountDataDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiscountDataDto {

        Long menuId;
        Integer discountPrice;
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
