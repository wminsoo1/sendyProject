package com.example.wallet.exception;

import com.example.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum WalletErrorCode implements ErrorCode {

    WALLET_NOT_FOUND("지갑이 존재하지 않습니다", HttpStatus.NOT_FOUND),
    EXCEEDED_MAX_WALLET_COUNT("지갑의 최대 개수를 초과하였습니다.", HttpStatus.NOT_FOUND),
    NEGATIVE_OR_ZERO_CHARGE_AMOUNT("충전 금액은 0보다 커야 합니다.", HttpStatus.BAD_REQUEST),
    NEGATIVE_OR_ZERO_PAYMENT_AMOUNT("결제 금액은 0보다 커야 합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE("잔액이 부족합니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    WalletErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
