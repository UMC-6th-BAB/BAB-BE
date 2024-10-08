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
    INVALID_SIGNATURE_MENU_COUNT(HttpStatus.NOT_FOUND, "MENU401", "유효하지 않은 시그니처 메뉴 개수입니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU404", "해당 메뉴가 존재하지않습니다."),

    // Store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE404", "해당하는 가게가 존재하지않습니다."),
    STORE_BAD_REQUEST(HttpStatus.BAD_REQUEST, "STORE400", "이미 등록된 주소입니다."),

    // Operating Hours
    OPERATING_HOURS_NOT_FOUND(HttpStatus.NOT_FOUND, "OH404", "운영시간을 등록하지 않았습니다."),

    // Discount
    DISCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "DISCOUNT401", "해당 할인 행사가 존재하지 않습니다."),
    DISCOUNT_STORE_NOT_MATCH(HttpStatus.BAD_REQUEST, "DISCOUNT402", "해당 가게의 할인 행사가 아닙니다."),
    DISCOUNT_TIME_DUPLICATION(HttpStatus.CONFLICT, "DISCOUNT403", "해당 날짜에 이미 존재하는 할인 행사가 있습니다."),

    // Owner
    OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "OWNER404", "사장님 정보가 등록되어 있지 않습니다."),
    OWNER_NOT_HAVE_STORE(HttpStatus.BAD_REQUEST, "OWNER405", "사장님이 보유중인 가게가 없습니다."),

    //Student
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "OWNER404", "학생 정보가 등록되어 있지 않습니다."),

    // University
    UNIVERSITY_NOT_FOUND(HttpStatus.NOT_FOUND, "UNIVERSITY404", "등록되지 않은 학교입니다."),
    UNIVERSITY_NOT_SETTING(HttpStatus.NOT_FOUND, "UNIVERSITY405", "대학교 설정이 되어있지 않습니다."),

    // S3 Storage
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3BUCKET500", "파일 업로드에 실패했습니다."),

    // SignatureMenu
    SIGNATURE_MENU_NOT_EXIST(HttpStatus.NOT_FOUND, "SIGNATURE401", "해당 가게의 대표 메뉴가 설정되어 있지 않습니다."),

    STORE_UNIVERSITY_NOT_FOUND(HttpStatus.NOT_FOUND, "STOREUNIVERSITY401", "가게와 연결된 대학교가 존재하지 않습니다."),

    // OCR
    OCR_BAD_REQUEST(HttpStatus.BAD_REQUEST, "OCR402","아무것도 인식하지 못했습니다.")
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
