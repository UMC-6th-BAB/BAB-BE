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

    public static DiscountResponseDto.DeleteDiscountResponseDto toDeleteDiscountResponseDto(Discount deletedDiscount) {

        return DiscountResponseDto.DeleteDiscountResponseDto.builder()
                .id(deletedDiscount.getId())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static DiscountResponseDto.CreateDiscountResponseDto toCreateDiscountResponseDto(Discount discount) {

        List<DiscountResponseDto.CreateDiscountMenuResponseDto> createDiscountMenuResponseDtoList = discount.getDiscountMenuList().stream()
                .map(discountMenu -> {

                    return DiscountResponseDto.CreateDiscountMenuResponseDto.builder()
                            .menuId(discountMenu.getMenu().getId())
                            .menuName(discountMenu.getMenu().getMenuName())
                            .menuPrice(discountMenu.getMenu().getPrice())
                            .discountPrice(discountMenu.getDiscountPrice())
                            .build();
                }).collect(Collectors.toList());

        return DiscountResponseDto.CreateDiscountResponseDto.builder()
                .storeId(discount.getStore().getId())
                .storeName(discount.getStore().getName())
                .discountId(discount.getId())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .title(discount.getTitle())
                .inProgress(discount.getInProgress())
                .createdAt(LocalDateTime.now())
                .createDiscountMenuDataDtoList(createDiscountMenuResponseDtoList)
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
