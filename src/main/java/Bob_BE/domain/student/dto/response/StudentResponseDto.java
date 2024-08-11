package Bob_BE.domain.student.dto.response;

import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.university.dto.response.UniversityResponseDto;
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
    public static class myPageDto {
        private Boolean isUniversityExist;
        private UniversityResponseDto.UniversityBaseInfoDto university;

        private List<StoreResponseDto.GetOnSaleStoreInMyPageDto> todayMenus;
        private AccountDto account;
    }


    @Getter
    @Builder
    public static class AccountDto {
        private String name;

        private String email;
    }
}
