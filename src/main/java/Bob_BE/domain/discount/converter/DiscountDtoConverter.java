package Bob_BE.domain.discount.converter;

import Bob_BE.domain.discount.dto.parameter.DiscountParameterDto;
import Bob_BE.domain.discount.dto.request.DiscountRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DiscountDtoConverter {

    DiscountDtoConverter INSTANCE = Mappers.getMapper(DiscountDtoConverter.class);

    DiscountParameterDto.CreateDiscountParamDto toCreateDiscountParamDto(DiscountRequestDto.CreateDiscountRequestDto request, Long storeId);
    DiscountParameterDto.DeleteDiscountParamDto toDeleteDiscountParamDto(Long storeId, Long discountId);
    DiscountParameterDto.GetDiscountedListParamDto toGetDiscountedListParamDto(Long storeId);
}
