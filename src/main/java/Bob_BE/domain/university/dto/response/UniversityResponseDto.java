package Bob_BE.domain.university.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UniversityResponseDto {
    @Getter
    @Builder
    public static class UniversityBaseInfoDto {
        private String universityName;
        private String universityAddress;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UniversitySearchDto{
        private Long universityId;
        private String universityName;
        private String universityLogo;
        private String universityAddress;
    }


}
