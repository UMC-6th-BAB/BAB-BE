package Bob_BE.domain.owner.converter;

import Bob_BE.domain.owner.dto.response.OwnerResponseDto;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.store.entity.Store;

public class OwnerConverter {

    public static OwnerResponseDto.OwnerMyPageResponseDto toOwnerMyPageResponseDto(Owner owner, Store store) {

        boolean isStoreExist;
        boolean isUniversitySetting;
        if (store.getId() == null) {

            isStoreExist = false;

            isUniversitySetting = false;
        }
        else {

            isStoreExist = true;

            if (store.getStoreUniversityList().isEmpty()) {

                isUniversitySetting = false;
            }
            else isUniversitySetting = true;
        }

        return OwnerResponseDto.OwnerMyPageResponseDto.builder()
                .ownerId(owner.getId())
                .ownerNickname(owner.getEmail().substring(0, owner.getEmail().indexOf('@')))
                .isStoreExist(isStoreExist)
                .storeId(store.getId())
                .storeName(store.getName())
                .isUniversitySetting(isUniversitySetting)
                .build();
    }
}
