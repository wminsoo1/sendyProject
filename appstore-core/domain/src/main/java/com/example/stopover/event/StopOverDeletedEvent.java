package com.example.stopover.event;

import lombok.Getter;

@Getter
public class StopOverDeletedEvent {

    private final String reservationNumber;

    public StopOverDeletedEvent(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }
}
