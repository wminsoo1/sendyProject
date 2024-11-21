package com.example.wallet.exception;


import com.example.global.exception.CustomException;
import com.example.global.exception.ErrorCode;

public class WalletException extends CustomException {
    public WalletException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static WalletException fromErrorCode(final ErrorCode errorCode) {
        return new WalletException(errorCode);
    }
}
