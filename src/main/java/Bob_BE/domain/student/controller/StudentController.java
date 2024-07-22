package Bob_BE.domain.student.controller;

import Bob_BE.domain.student.dto.request.StudentRequestDto;
import Bob_BE.domain.student.dto.response.StudentResponseDto;
import Bob_BE.domain.student.service.StudentService;
import Bob_BE.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/student")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "학생 카카오 소셜 로그인 및 회원가입",
            description = "학생이 카카오 소셜 로그인을 진행합니다." +
                    "<br>로그인 기록이 없으면 회원가입을 진행합니다." +
                    "<br>kakao developer에서 예시 token을 받아올 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "COMMON200: 학생 로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "COMMON201: 학생 회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "GLOBAL402: 카카오 토큰관련 서버 에러"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "GLOBAL401: 서버 에러")})
    public ApiResponse<?> registerOrLoginStudent(@RequestBody StudentRequestDto.LoginOrRegisterDto request){
        StudentResponseDto.LoginOrRegisterDto response = studentService.registerOrLogin(request);
        return ApiResponse.of(response.getSuccessStatus(), response);
    }
}
