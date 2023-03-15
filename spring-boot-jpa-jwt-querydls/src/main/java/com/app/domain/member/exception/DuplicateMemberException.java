package com.app.domain.member.exception;


import com.app.global.error.ErrorCode;
import com.app.global.error.exception.BusinessException;

public class DuplicateMemberException extends BusinessException {

    public DuplicateMemberException() {
        super(ErrorCode.ALREADY_REGISTERED_MEMBER);
    }
}
