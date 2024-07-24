package Bob_BE.domain.student.converter;


import Bob_BE.domain.student.dto.response.StudentResponseDto;
import Bob_BE.domain.student.entity.Student;
import Bob_BE.domain.university.entity.University;

public class StudentConverter {
    public static StudentResponseDto.updateUniversityDto toUpdateUniversityDto(Student student){
        return StudentResponseDto.updateUniversityDto
                .builder()
                .message("성공")
                .build();
    }

    public static StudentResponseDto.myPageDto toMyPageDto(Student student) {
        StudentResponseDto.AccountDto accountDto = StudentResponseDto.AccountDto
                .builder()
                .name(student.getNickname())
                .email(student.getEmail())
                .build();
        University university = student.getUniversity();
        if(university == null){
            return StudentResponseDto.myPageDto
                    .builder()
                    .account(accountDto)
                    .isUniversityExist(false)
                    .build();
        } else {
            StudentResponseDto.UniversityDto universityDto = StudentResponseDto.UniversityDto
                    .builder()
                    .universityName(university.getUniversityName())
                    .universityAddress(university.getAddress())
                    .build();
            return StudentResponseDto.myPageDto
                    .builder()
                    .account(accountDto)
                    .isUniversityExist(true)
                    .university(universityDto)
                    .build();
        }


    }
}

