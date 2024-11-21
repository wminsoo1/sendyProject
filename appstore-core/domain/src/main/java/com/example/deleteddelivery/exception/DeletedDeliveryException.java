package com.example.deleteddelivery.exception;


import com.example.global.exception.CustomException;
import com.example.global.exception.ErrorCode;

public class DeletedDeliveryException extends CustomException {
    public DeletedDeliveryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static DeletedDeliveryException fromErrorCode(final ErrorCode errorCode) {
        return new DeletedDeliveryException(errorCode);
    }
}
