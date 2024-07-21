package Bob_BE.domain.discount.dto.parameter;

import Bob_BE.domain.discount.dto.data.DiscountDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class DiscountParameterDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiscountParamDto {

        List<DiscountDataDto.CreateDiscountDataDto> createDiscountDataDtoList;
        Long storeId;
    }
}
