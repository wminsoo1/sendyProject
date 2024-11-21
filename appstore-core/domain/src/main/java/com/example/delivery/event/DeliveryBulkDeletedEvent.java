package com.example.delivery.event;

import lombok.Getter;

@Getter
public class DeliveryBulkDeletedEvent {

    private final String reservationNumber;

    public DeliveryBulkDeletedEvent(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

}
