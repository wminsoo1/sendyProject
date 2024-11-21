package com.example.deleteddelivery.event;

import com.example.deleteddelivery.entity.DeletedDelivery;
import java.util.List;
import lombok.Getter;

@Getter
public class DeletedDeliveriesSavedEvent {

    private final List<DeletedDelivery> deletedDeliveries;

    public DeletedDeliveriesSavedEvent(List<DeletedDelivery> deletedDeliveries) {
        this.deletedDeliveries = deletedDeliveries;
    }

}
