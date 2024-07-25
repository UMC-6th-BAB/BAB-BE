package Bob_BE.global.response.code.resultCode;


import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL401", "서버 오류"),
    KAKAO_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "GLOBAL402", "토큰관련 서버 에러"),

    // OAuth
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "OAUTH401", "JWT 토큰 만료"),
    MISSING_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "OAUTH404", "JWT 토큰 없음"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER401", "해당 유저가 존재하지 않습니다." ),

    // Menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU404", "해당 메뉴가 존재하지않습니다."),

    // Store
    STORE_NOT_FOUND(HttpStatus.BAD_REQUEST, "STORE404", "해당하는 가게가 존재하지않습니다."),

    // Discount
    DISCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "DISCOUNT401", "해당 할인 행사가 존재하지 않습니다."),
    DISCOUNT_STORE_NOT_MATCH(HttpStatus.BAD_REQUEST, "DISCOUNT402", "해당 가게의 할인 행사가 아닙니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
