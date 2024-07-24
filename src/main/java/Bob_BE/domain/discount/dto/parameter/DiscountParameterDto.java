package Bob_BE.domain.discount.dto.parameter;

import Bob_BE.domain.discount.dto.data.DiscountDataDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DiscountParameterDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiscountParamDto {

        private List<DiscountDataDto.CreateDiscountDataDto> discountMenuDataDtoList;
        @NotNull
        private String title;
        @NotNull
        private LocalDate startDate;
        @NotNull
        private LocalDate endDate;
        @NotNull
        private Long storeId;
    }
}
