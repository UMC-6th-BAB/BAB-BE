package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;

public class SignatureMenuHandler extends GeneralException {

    public SignatureMenuHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
