package com.example.stopover.eventlistener;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.dto.request.DeliverySaveRequest;
import com.example.delivery.model.entity.Delivery;
import com.example.stopover.event.StopOverBulkDeletedEvent;
import com.example.stopover.event.StopOverDeletedEvent;
import com.example.stopover.event.StopOverSavedEvent;
import com.example.stopover.event.StopOverUpdatedEvent;
import com.example.stopover.service.StopOverService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StopOverEventListener {

    private final StopOverService stopOverService;

    @EventListener
    public void handleStopOverSavedEvent(StopOverSavedEvent stopOverSavedEvent) {
        Delivery delivery = stopOverSavedEvent.getDelivery();
        DeliverySaveRequest deliverySaveRequest = stopOverSavedEvent.getDeliverySaveRequest();

        stopOverService.saveStopOver(delivery, deliverySaveRequest);
    }

    @EventListener
    public void handleStopOverDeletedEvent(StopOverDeletedEvent stopOverDeletedEvent) {
        String reservationNumber = stopOverDeletedEvent.getReservationNumber();

        stopOverService.deleteStopOver(reservationNumber);
    }

    @EventListener
    public void handleStopOverBulkDeletedEvent(StopOverBulkDeletedEvent stopOverBulkDeletedEvent) {
        String reservationNumber = stopOverBulkDeletedEvent.getReservationNumber();

        stopOverService.deleteStopOverInBulk(reservationNumber);
    }

    @EventListener
    public void handleStopOverUpdatedEvent(StopOverUpdatedEvent stopOverUpdatedEvent) {
        Delivery delivery = stopOverUpdatedEvent.getDelivery();
        List<DeliveryAddress> stopOverAddresses = stopOverUpdatedEvent.getStopOverAddresses();

        stopOverService.updateStopOver(delivery, stopOverAddresses);
    }

}


