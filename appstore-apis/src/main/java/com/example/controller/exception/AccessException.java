package com.example.controller.exception;

import com.example.global.exception.CustomException;
import com.example.global.exception.ErrorCode;

public class AccessException extends CustomException {
    public AccessException(ErrorCode errorCode) {
        super(errorCode);
    }
    public static AccessException fromErrorCode(final ErrorCode errorCode) {
        return new AccessException(errorCode);
    }
}
