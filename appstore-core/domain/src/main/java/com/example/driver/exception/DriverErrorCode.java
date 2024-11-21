package com.example.driver.exception;

import com.example.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DriverErrorCode implements ErrorCode {

    DRIVER_NOT_FOUND("운전자가 존재하지 않습니다", HttpStatus.NOT_FOUND),
    DRIVER_DUPLICATE_NAME("운전자 이름이 이미 존재합니다", HttpStatus.CONFLICT),
    NO_DELIVERIES_MATCHING_VEHICLE("현재 탈것과 매칭되는 배달이 없습니다", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

    DriverErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}


