package Bob_BE.domain.store.converter;

import Bob_BE.domain.store.dto.parameter.StoreParameterDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoreDtoConverter {

    StoreDtoConverter INSTANCE = Mappers.getMapper(StoreDtoConverter.class);

    StoreParameterDto.GetMenuNameListParamDto toGetMenuNameListParamDto(Long storeId);
    StoreParameterDto.GetOnSaleStoreListParamDto toGetOnSaleStoreListParamDto(Long universityId);
}
