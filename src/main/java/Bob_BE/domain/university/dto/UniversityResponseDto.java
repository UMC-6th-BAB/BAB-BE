package Bob_BE.domain.university.dto;

import lombok.Builder;
import lombok.Getter;

public class UniversityResponseDto {
    @Getter
    @Builder
    public static class UniversityBaseInfoDto {
        private String universityName;
        private String universityAddress;
    }
}
