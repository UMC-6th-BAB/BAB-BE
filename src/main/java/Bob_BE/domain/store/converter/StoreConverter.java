package Bob_BE.domain.store.converter;

import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.Store;

public class StoreConverter {

    public static StoreResponseDto.StoreCreateResultDto toCreateStoreResponseDto(Store store){

        return StoreResponseDto.StoreCreateResultDto.builder()
                .id(store.getId())
                .name(store.getName())
                .build();
    }

    public static Store toStore(Owner owner, StoreRequestDto.StoreCreateRequestDto requestDto){

        return Store.builder()
                .owner(owner)
                .longitude(requestDto.getLongitude())
                .latitude(requestDto.getLatitude())
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .streetAddress(requestDto.getStreetAddress())
                .storeLink(requestDto.getStoreLink())
                .registration(requestDto.getRegistration())
                .build();
    }
}
