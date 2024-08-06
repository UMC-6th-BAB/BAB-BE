package Bob_BE.domain.owner.converter;

import Bob_BE.domain.owner.dto.parameter.OwnerParameterDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OwnerDtoConverter {

    OwnerDtoConverter INSTANCE = Mappers.getMapper(OwnerDtoConverter.class);

    OwnerParameterDto.OwnerMyPageParamDto toOwnerMyPageParamDto(Long ownerId);
}
