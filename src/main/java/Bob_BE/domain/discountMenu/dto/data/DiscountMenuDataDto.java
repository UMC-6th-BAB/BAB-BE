package Bob_BE.domain.discountMenu.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DiscountMenuDataDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiscountMenuDataDto {

        private Long menuId;
        private Integer discountPrice;
    }
}
