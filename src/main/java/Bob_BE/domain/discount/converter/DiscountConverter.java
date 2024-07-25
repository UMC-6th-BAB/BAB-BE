package Bob_BE.domain.discount.converter;

import Bob_BE.domain.discount.dto.parameter.DiscountParameterDto;
import Bob_BE.domain.discount.dto.response.DiscountResponseDto;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.store.entity.Store;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DiscountConverter {

    public static DiscountResponseDto.CreateDiscountResponseDto toCreateDiscountResponseDto(Discount discount) {

        return DiscountResponseDto.CreateDiscountResponseDto.builder()
                .discountId(discount.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Discount toDiscount (DiscountParameterDto.CreateDiscountParamDto param, Store store) {

        return Discount.builder()
                .title(param.getTitle())
                .startDate(param.getStartDate())
                .endDate(param.getEndDate())
                .store(store)
                .discountMenuList(new ArrayList<>())
                .build();
    }
}