package Bob_BE.domain.discount.converter;

import Bob_BE.domain.discount.dto.parameter.DiscountParameterDto;
import Bob_BE.domain.discount.dto.response.DiscountResponseDto;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.store.entity.Store;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscountConverter {

    public static DiscountResponseDto.GetDiscountedListResponseDto toGetDiscountedListResponseDto(List<Discount> discountList) {

        List<DiscountResponseDto.GetDiscountDataDto> getDiscountDataDtoList = discountList.stream()
                .map(discount -> {
                    return DiscountResponseDto.GetDiscountDataDto.builder()
                            .discountId(discount.getId())
                            .storeName(discount.getStore().getName())
                            .discountTitle(discount.getTitle())
                            .startDate(discount.getStartDate())
                            .endDate(discount.getEndDate())
                            .build();
                }).collect(Collectors.toList());

        return DiscountResponseDto.GetDiscountedListResponseDto.builder()
                .getDiscountDataDtoList(getDiscountDataDtoList)
                .totalDataNum(getDiscountDataDtoList.size())
                .build();
    }

    public static DiscountResponseDto.DeleteDiscountResponseDto toDeleteDiscountResponseDto() {

        return DiscountResponseDto.DeleteDiscountResponseDto.builder()
                .message("삭제 성공.")
                .deletedAt(LocalDateTime.now())
                .build();
    }

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
