package Bob_BE.domain.owner.dto.response;

import Bob_BE.global.response.code.resultCode.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OwnerResponseDto {
    @Getter
    @Builder
    public static class LoginOrRegisterDto {
        private SuccessStatus successStatus;
        private String jwt;
        private String kakaoNickname;
        private String kakaoEmail;
        private String role;
        private Boolean isStoreExist;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OwnerMyPageResponseDto {

        private Long ownerId;
        private String ownerNickname;
        private Long storeId;
        private String storeName;
    }
}
