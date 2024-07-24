package Bob_BE.domain.student.converter;


import Bob_BE.domain.student.dto.response.StudentResponseDto;
import Bob_BE.domain.student.entity.Student;

public class StudentConverter {
    public static StudentResponseDto.updateUniversityDto toUpdateUniversityDto(Student student){
        return StudentResponseDto.updateUniversityDto
                .builder()
                .message("성공")
                .build();
    }
}

