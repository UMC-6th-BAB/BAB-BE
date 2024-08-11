package Bob_BE.global.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

public class KakaoResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class KakaoTokenResponseDto {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("expires_in")
        private int expiresIn;
        private String scope;
        @JsonProperty("refresh_token_expires_in")
        private int refreshTokenExpiresIn;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoUserResponseDto {
        private Long id;
        private String connected_at;
        private Properties properties;
        private KakaoAccount kakao_account;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Properties {
            private String nickname;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class KakaoAccount {
            private boolean profile_nickname_needs_agreement;
            private Profile profile;
            private boolean has_email;
            private boolean email_needs_agreement;
            private boolean is_email_valid;
            private boolean is_email_verified;
            private String email;

            @Getter
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Profile {
                private String nickname;
                private boolean is_default_nickname;
            }
        }
    }
}
