package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;

public class StoreUniversityHandler extends GeneralException {
    public StoreUniversityHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
