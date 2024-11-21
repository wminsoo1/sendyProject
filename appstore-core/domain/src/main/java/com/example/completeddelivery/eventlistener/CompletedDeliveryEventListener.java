package com.example.completeddelivery.eventlistener;

import com.example.completeddelivery.event.CompletedDeliverySavedEvent;
import com.example.completeddelivery.service.CompletedDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompletedDeliveryEventListener {

    private final CompletedDeliveryService completedDeliveryService;

    @EventListener
    public void handleCompletedDeliverySavedEvent(CompletedDeliverySavedEvent completedDeliverySavedEvent) {
        completedDeliveryService.saveCompletedDelivery(completedDeliverySavedEvent.getCompletedDelivery());
    }

}
