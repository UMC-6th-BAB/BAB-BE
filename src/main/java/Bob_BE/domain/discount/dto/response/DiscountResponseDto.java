package Bob_BE.domain.discount.dto.response;

import Bob_BE.domain.discountMenu.entity.DiscountMenu;
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

        private Long storeId;
        private String storeName;
        private Long discountId;
        private LocalDate startDate;
        private LocalDate endDate;
        private String title;
        private Boolean inProgress;
        private LocalDateTime createdAt;
        private List<CreateDiscountMenuResponseDto> createDiscountMenuDataDtoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiscountMenuResponseDto {

        private Long menuId;
        private String menuName;
        private Integer menuPrice;
        private Integer discountPrice;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteDiscountResponseDto {

        private Long id;
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
