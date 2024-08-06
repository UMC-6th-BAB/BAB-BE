package Bob_BE.global.response.code;

public interface BaseErrorCode {
    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();
}
