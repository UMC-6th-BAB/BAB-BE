package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;

public class OwnerHandler extends GeneralException {

    public OwnerHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
