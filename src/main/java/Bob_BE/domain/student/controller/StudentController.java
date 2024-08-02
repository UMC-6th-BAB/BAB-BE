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

    @PatchMapping("/university")
    @Operation(summary = "학생 대학교 등록 및 수정",
            description = "학생의 대학교를 변경합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "COMMON200: 학생 대학 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "OAUTH401: JWT 토큰 만료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "OAUTH404: JWT 토큰 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "GLOBAL401: 서버 에러")})
    public ApiResponse<?> updateStudentUniversity(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestBody StudentRequestDto.updateUniversityDto request){
        Long userId = studentService.getUserIdFromJwt(authorizationHeader);

        StudentResponseDto.myPageDto response = studentService.updateUniversity(userId, request);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/mypage")
    @Operation(summary = "학생의 마이페이지 조회",
            description = "학생의 마이페이지 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "COMMON200: 학생 마이페이지 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "OAUTH401: JWT 토큰 만료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "OAUTH404: JWT 토큰 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "GLOBAL401: 서버 에러")})
    public ApiResponse<?> getMyPage(@RequestHeader(value = "Authorization", required = false) String authorizationHeader){
        Long userId = studentService.getUserIdFromJwt(authorizationHeader);

        StudentResponseDto.myPageDto response = studentService.getMyPage(userId);
        return ApiResponse.onSuccess(response);
    }
}
