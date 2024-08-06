package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;

public class StoreHandler extends GeneralException {

    public StoreHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
