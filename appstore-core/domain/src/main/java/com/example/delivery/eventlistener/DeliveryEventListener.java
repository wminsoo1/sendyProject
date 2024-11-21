package com.example.delivery.eventlistener;

import com.example.delivery.event.DeliveriesBulkDeletedEvent;
import com.example.delivery.event.DeliveryBulkDeletedEvent;
import com.example.delivery.model.entity.Delivery;
import com.example.delivery.service.DeliveryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventListener {

    private final DeliveryService deliveryService;

    @EventListener
    public void handleCompletedDeliverySavedEvent(DeliveryBulkDeletedEvent deliveryBulkDeletedEvent) {
        deliveryService.deleteDeliveryInBulk(deliveryBulkDeletedEvent.getReservationNumber());
    }

    @EventListener
    public void handleCompletedDeliveriesSavedEvent(DeliveriesBulkDeletedEvent deliveriesBulkDeletedEvent) {
        deliveryService.deleteDeliveriesInBulk(deliveriesBulkDeletedEvent.getDeliveries());
    }

}
