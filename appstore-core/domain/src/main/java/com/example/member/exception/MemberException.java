package com.example.member.exception;


import com.example.global.exception.CustomException;
import com.example.global.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static MemberException fromErrorCode(final ErrorCode errorCode) {
        return new MemberException(errorCode);
    }
}
