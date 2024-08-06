package Bob_BE.global.response.exception.handler;

import Bob_BE.global.response.code.BaseErrorCode;
import Bob_BE.global.response.exception.GeneralException;


public class ImageHandler extends GeneralException {
    public ImageHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
