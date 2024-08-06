package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;

public class DiscountHandler extends GeneralException {

    public DiscountHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
