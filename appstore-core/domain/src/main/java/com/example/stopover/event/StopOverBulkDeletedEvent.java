package com.example.stopover.event;

import lombok.Getter;

@Getter
public class StopOverBulkDeletedEvent {

    private final String reservationNumber;

    public StopOverBulkDeletedEvent(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }
}
