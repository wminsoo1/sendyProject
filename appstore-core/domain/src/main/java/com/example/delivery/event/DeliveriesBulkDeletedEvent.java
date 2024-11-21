package com.example.delivery.event;

import com.example.delivery.model.entity.Delivery;
import java.util.List;
import lombok.Getter;

@Getter
public class DeliveriesBulkDeletedEvent {

    private final List<Delivery> deliveries;

    public DeliveriesBulkDeletedEvent(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

}
