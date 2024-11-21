package com.example.deleteddelivery.eventlistener;

import com.example.deleteddelivery.event.DeletedDeliveriesSavedEvent;
import com.example.deleteddelivery.service.DeletedDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeletedDeliveryEventListener {

    private final DeletedDeliveryService deletedDeliveryService;

    @EventListener
    public void handleCompletedDeliverySavedEvent(DeletedDeliveriesSavedEvent deletedDeliveriesSavedEvent) {
        deletedDeliveryService.saveDeletedDeliveries(deletedDeliveriesSavedEvent.getDeletedDeliveries());
    }

}
