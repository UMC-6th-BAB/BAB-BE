package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;

public class StudentHandler extends GeneralException {

    public StudentHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
