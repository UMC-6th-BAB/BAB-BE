package Bob_BE.domain.discount.dto.request;

import Bob_BE.domain.discount.dto.data.DiscountDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class DiscountRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiscountRequestDto {

        List<DiscountDataDto.CreateDiscountDataDto> discountMenuDataDtoList;
    }
}
