package Bob_BE.domain.student.dto.response;

import Bob_BE.global.response.code.resultCode.SuccessStatus;
import lombok.Builder;
import lombok.Getter;

public class StudentResponseDto {
    @Getter
    @Builder
    public static class LoginOrRegisterDto{
        private SuccessStatus successStatus;
        private String jwt;
    }
}
