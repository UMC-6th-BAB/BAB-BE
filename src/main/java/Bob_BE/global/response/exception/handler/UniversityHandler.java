package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;

public class UniversityHandler extends GeneralException {

    public UniversityHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
