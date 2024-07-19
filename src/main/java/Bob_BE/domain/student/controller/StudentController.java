package Bob_BE.domain.student.controller;

import Bob_BE.domain.student.dto.request.StudentRequestDto;
import Bob_BE.domain.student.dto.response.StudentResponseDto;
import Bob_BE.domain.student.service.StudentService;
import Bob_BE.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/student")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerOrLoginStudent(@RequestBody StudentRequestDto.LoginOrRegisterDto request){
        StudentResponseDto.LoginOrRegisterDto response = studentService.registerOrLogin(request);
        ApiResponse<StudentResponseDto.LoginOrRegisterDto> apiResponse
                = ApiResponse.of(response.getSuccessStatus(), response);
        return new ResponseEntity<>(apiResponse, response.getSuccessStatus().getHttpStatus());
    }
}
