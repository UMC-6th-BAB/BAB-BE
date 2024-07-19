package Bob_BE.global.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KakaoResponseDto {
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
