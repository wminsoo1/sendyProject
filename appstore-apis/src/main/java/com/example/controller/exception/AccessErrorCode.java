package com.example.controller.exception;

import com.example.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AccessErrorCode implements ErrorCode {

    USER_ACCESS("사용자와 관리자만 권한이 있습니다", HttpStatus.FORBIDDEN),
    DRIVER_ACCESS("운전자와 관리자만 권한이 있습니다", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_ACCESS("인증이 필요합니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;

    AccessErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}


