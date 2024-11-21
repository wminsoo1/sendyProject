package com.example.delivery.exception;

import com.example.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DeliveryErrorCode implements ErrorCode {

    DELIVERY_NOT_FOUND("배달이 존재하지 않습니다", HttpStatus.NOT_FOUND),
    STOPOVER_NOT_FOUND("경유지가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    RESERVATION_NUMBER_NOT_FOUND("배달 예약 번호가 존재하지 않습니다", HttpStatus.NOT_FOUND),
    RESERVATION_NUMBER_DUPLICATED("배달 예약 번호가 이미 존재합니다", HttpStatus.NOT_FOUND),
    DELIVERY_ADDRESS_NOT_FOUND("배달 주소가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    VEHICLE_NOT_FOUND("탈것이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_ID_RESERVATION_NUMBER_MISMATCH("멤버와 예약 번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND);


    private final String message;
    private final HttpStatus httpStatus;

    DeliveryErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
