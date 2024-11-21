package com.example.driver.exception;

import com.example.global.exception.CustomException;
import com.example.global.exception.ErrorCode;

public class DriverException extends CustomException {
    public DriverException(ErrorCode errorCode) {
        super(errorCode);
    }
    public static DriverException fromErrorCode(final ErrorCode errorCode) {
        return new DriverException(errorCode);
    }
}
