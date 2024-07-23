package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;

public class MenuHandler extends GeneralException {

    public MenuHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
