package Bob_BE.domain.owner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OwnerRequestDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginOrRegisterDto {
        @Schema(description = "카카오 토큰")
        @NotNull
        private String token;
    }
}
