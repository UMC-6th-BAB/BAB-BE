package Bob_BE.domain.owner.controller;

import Bob_BE.domain.owner.dto.request.OwnerRequestDto;
import Bob_BE.domain.owner.dto.response.OwnerResponseDto;
import Bob_BE.domain.owner.service.OwnerService;
import Bob_BE.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/owner")
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping
    @Operation(summary = "사장 카카오 소셜 로그인 및 회원가입",
            description = "가게 사장이 카카오 소셜 로그인을 진행합니다." +
                    "<br>로그인 기록이 없으면 회원가입을 진행합니다." +
                    "<br>kakao developer에서 예시 token을 받아올 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "COMMON200: 사장 로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "COMMON201: 사장 회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "GLOBAL402: 카카오 토큰관련 서버 에러"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "GLOBAL401: 서버 에러")})
    public ApiResponse<?> registerOrLoginOwner(@RequestBody OwnerRequestDto.LoginOrRegisterDto request){
        OwnerResponseDto.LoginOrRegisterDto response = ownerService.registerOrLogin(request);
        return ApiResponse.of(response.getSuccessStatus(), response);
    }
}
