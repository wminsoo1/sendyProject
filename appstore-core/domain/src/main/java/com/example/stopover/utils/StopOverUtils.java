package com.example.stopover.utils;

import com.example.delivery.model.DeliveryAddress;
import com.example.stopover.entity.StopOver;
import java.util.List;

public class StopOverUtils {

    private StopOverUtils() {
    }

    public static List<DeliveryAddress> findDeliveryAddressToAdd(List<DeliveryAddress> stopOverAddresses, List<StopOver> stopOverByDeliveryId) {
        return stopOverAddresses.stream()
            .filter(deliveryAddress -> stopOverByDeliveryId.stream()
                .noneMatch(stopOver -> stopOver.getDeliveryAddress().equals(deliveryAddress)))
            .toList();
    }

    public static List<StopOver> findStopOversToDelete(List<StopOver> stopOverByDeliveryId, List<DeliveryAddress> stopOverAddresses) {
        return stopOverByDeliveryId.stream()
            .filter(stopOver -> stopOverAddresses.stream()
                .noneMatch(deliveryAddress -> deliveryAddress.equals(stopOver.getDeliveryAddress())))
            .toList();
    }
}
