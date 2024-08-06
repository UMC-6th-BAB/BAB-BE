package Bob_BE.domain.owner.converter;

import Bob_BE.domain.owner.dto.response.OwnerResponseDto;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.store.entity.Store;

public class OwnerConverter {

    public static OwnerResponseDto.OwnerMyPageResponseDto toOwnerMyPageResponseDto(Owner owner, Store store) {

        return OwnerResponseDto.OwnerMyPageResponseDto.builder()
                .ownerId(owner.getId())
                .ownerNickname(owner.getEmail().substring(0, owner.getEmail().indexOf('@')))
                .storeId(store.getId())
                .storeName(store.getName())
                .build();
    }
}
