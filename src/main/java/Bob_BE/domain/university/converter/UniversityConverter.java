package Bob_BE.domain.university.converter;

import Bob_BE.domain.university.dto.response.UniversityResponseDto;
import Bob_BE.domain.university.entity.University;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class UniversityConverter {

    public static UniversityResponseDto.UniversitySearchDto toUniversitySearchDto(University university){
        return UniversityResponseDto.UniversitySearchDto.builder()
                .universityId(university.getId())
                .universityName(university.getUniversityName())
                .universityLogo(university.getUniversityLogo())
                .universityAddress(university.getAddress())
                .build();
    }
    public static List<UniversityResponseDto.UniversitySearchDto> toUniversitySearchDtoList(List<University> universityList){
        return universityList.stream()
                .map(UniversityConverter::toUniversitySearchDto)
                .collect(toList());
    }
}
