package Bob_BE.domain.student.dto.response;

import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.global.response.code.resultCode.SuccessStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class StudentResponseDto {
    @Getter
    @Builder
    public static class LoginOrRegisterDto{
        private SuccessStatus successStatus;
        private String jwt;
    }

    @Getter
    @Builder
    public static class updateUniversityDto {
        private String message;
    }

    @Getter
    @Builder
    public static class myPageDto {
        private Boolean isUniversityExist;
        private UniversityDto university;

        private List<StoreResponseDto.StoreAndDiscountDataDto> todayMenus;
        private AccountDto account;
    }

    @Getter
    @Builder
    public static class UniversityDto {
        private String universityName;
        private String universityAddress;
    }

    @Getter
    @Builder
    public static class AccountDto {
        private String name;

        private String email;
    }
}
