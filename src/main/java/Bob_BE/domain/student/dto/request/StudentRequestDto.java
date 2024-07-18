package Bob_BE.domain.student.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class StudentRequestDto {
    @Getter
    @Builder
    public static class LoginOrRegisterDto{
        @NotNull String token;
    }
}
