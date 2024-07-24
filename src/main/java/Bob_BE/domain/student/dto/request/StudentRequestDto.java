package Bob_BE.domain.student.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StudentRequestDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginOrRegisterDto{
        @Schema(description = "카카오 토큰")
        @NotNull private String token;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateUniversityDto {
        @Schema(description = "대학 이름")
        @NotNull
        private String universityName;

        @Schema(description = "대학 주소")
        @NotNull
        private String universityAddress;
    }
}
