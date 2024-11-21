package com.example.deleteddelivery.exception;

import com.example.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DeletedDeliveryErrorCode implements ErrorCode {

    DELETED_DELIVERY_NOT_FOUND("삭제배달이 존재하지 않습니다", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

    DeletedDeliveryErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
