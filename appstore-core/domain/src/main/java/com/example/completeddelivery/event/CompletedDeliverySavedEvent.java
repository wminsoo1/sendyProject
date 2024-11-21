package com.example.completeddelivery.event;

import com.example.completeddelivery.entity.CompletedDelivery;
import lombok.Getter;

@Getter
public class CompletedDeliverySavedEvent {

    private final CompletedDelivery completedDelivery;

    public CompletedDeliverySavedEvent(CompletedDelivery completedDelivery) {
        this.completedDelivery = completedDelivery;
    }

}
