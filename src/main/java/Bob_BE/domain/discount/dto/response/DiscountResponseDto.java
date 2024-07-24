package Bob_BE.domain.discount.dto.response;

import Bob_BE.domain.discount.dto.data.DiscountDataDto;
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

        Long discountId;
        LocalDateTime createdAt;
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

        private List<DiscountDataDto.GetDiscountDataDto> getDiscountDataDtoList;
        private Integer totalDataNum;
    }
}
