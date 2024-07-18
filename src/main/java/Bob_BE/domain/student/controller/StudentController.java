package Bob_BE.domain.student.controller;

import Bob_BE.domain.student.dto.request.StudentRequestDto;
import Bob_BE.domain.student.dto.response.StudentResponseDto;
import Bob_BE.domain.student.service.StudentService;
import Bob_BE.global.response.ApiResponse;
import Bob_BE.global.response.code.resultCode.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/student")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ApiResponse<?> registerOrLoginStudent(@RequestBody StudentRequestDto.LoginOrRegisterDto request){
        StudentResponseDto.LoginOrRegisterDto response = studentService.registerOrLogin(request);

        if(response.getSuccessStatus().equals(SuccessStatus._OK))
            return ApiResponse.onSuccess(response);
        return ApiResponse.of(response.getSuccessStatus(), response);
    }
}
