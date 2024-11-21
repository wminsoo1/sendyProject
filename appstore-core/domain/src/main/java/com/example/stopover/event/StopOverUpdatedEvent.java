package com.example.stopover.event;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.entity.Delivery;
import java.util.List;
import lombok.Getter;

@Getter
public class StopOverUpdatedEvent {

    private final Delivery delivery;
    private final List<DeliveryAddress> stopOverAddresses;

    public StopOverUpdatedEvent(Delivery delivery, List<DeliveryAddress> stopOverAddresses) {
        this.delivery = delivery;
        this.stopOverAddresses = stopOverAddresses;
    }
}
