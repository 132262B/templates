package com.app.api.access.validator;

import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;

public class PasswordValidator {

    public static void passwordCheck(String password1, String password2) {
        if(!password1.equals(password2))
            throw new BusinessException(ErrorCode.SIGNUP_PASSWORD_INCONSISTENCY);
    }
}
