package com.example.stopover.event;

import com.example.delivery.model.dto.request.DeliverySaveRequest;
import com.example.delivery.model.entity.Delivery;
import lombok.Getter;

@Getter
public class StopOverSavedEvent {

    private final Delivery delivery;
    private final DeliverySaveRequest deliverySaveRequest;

    public StopOverSavedEvent(Delivery delivery, DeliverySaveRequest deliverySaveRequest) {
        this.delivery = delivery;
        this.deliverySaveRequest = deliverySaveRequest;
    }
}
