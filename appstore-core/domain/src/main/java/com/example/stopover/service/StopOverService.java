package com.example.stopover.service;

import static com.example.delivery.exception.DeliveryErrorCode.STOPOVER_NOT_FOUND;
import static com.example.stopover.utils.StopOverUtils.findDeliveryAddressToAdd;
import static com.example.stopover.utils.StopOverUtils.findStopOversToDelete;

import com.example.delivery.exception.DeliveryException;
import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.DropLocation;
import com.example.delivery.model.PickUpLocation;
import com.example.delivery.model.dto.request.DeliverySaveRequest;
import com.example.delivery.model.entity.Delivery;
import com.example.global.navermap.NaverMapGeocode;
import com.example.global.navermap.dto.LocationResult;
import com.example.stopover.entity.StopOver;
import com.example.stopover.repository.StopOverRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StopOverService {

    private final StopOverRepository stopOverRepository;
    private final NaverMapGeocode naverMapGeocode;

    @Transactional
    public void saveStopOver(Delivery delivery, DeliverySaveRequest deliverySaveRequest) {
        List<DeliveryAddress> stopOverAddresses = deliverySaveRequest.getStopOverAddresses();
        if (CollectionUtils.isEmpty(stopOverAddresses)) {
            return;
        }

        final List<StopOver> stopOvers = deliverySaveRequest.toStopOvers();

        for (StopOver stopOver : stopOvers) {
            stopOver.updateDelivery(delivery);

            updateDeliveryAddressAsync(deliverySaveRequest.getDeliveryAddress(), stopOver);

            stopOverRepository.save(stopOver);
        }
    }

    @Transactional
    public void deleteStopOver(String reservationNumber) {
        List<StopOver> stopOvers = stopOverRepository.findStopOverByReservationNumber(reservationNumber);

        if (CollectionUtils.isEmpty(stopOvers)) {
            return;
        }

        stopOverRepository.deleteAll(stopOvers);
    }

    @Transactional
    public void deleteStopOverInBulk(String reservationNumber) {
        List<StopOver> stopOvers = stopOverRepository.findStopOverByReservationNumber(reservationNumber);

        if (CollectionUtils.isEmpty(stopOvers)) {
            return;
        }

        stopOverRepository.deleteAllInBulk(stopOvers);
    }

    @Transactional
    public void updateStopOver(Delivery delivery, List<DeliveryAddress> stopOverAddresses) {
        List<StopOver> stopOvers = stopOverRepository.findStopOverByReservationNumber(delivery.getReservationNumber());
        if (CollectionUtils.isEmpty(stopOvers)) {
            throw DeliveryException.fromErrorCode(STOPOVER_NOT_FOUND);
        }

        List<StopOver> stopOversToDelete = findStopOversToDelete(stopOvers, stopOverAddresses);
        List<DeliveryAddress> stopOversToAdd = findDeliveryAddressToAdd(stopOverAddresses, stopOvers);

        stopOverRepository.deleteAll(stopOversToDelete);
        for (DeliveryAddress deliveryAddress : stopOversToAdd) {
            StopOver stopOver = StopOver.builder()
                .deliveryAddress(deliveryAddress)
                .delivery(delivery)
                .build();

            updateDeliveryAddressAsync(deliveryAddress, stopOver);
            stopOverRepository.save(stopOver);
        }
    }

    private void updateDeliveryAddressAsync(DeliveryAddress deliveryAddress, StopOver stopOver) {
        String pickUpAddress = deliveryAddress.getPickupLocation().getPickUpAddress();
        String dropAddress = deliveryAddress.getDropLocation().getDropAddress();

        CompletableFuture<LocationResult> locationResultFuture = naverMapGeocode.getLocationResultFuture(pickUpAddress, dropAddress);
        LocationResult locationResult;
        try {
            locationResult = locationResultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        PickUpLocation pickUpLocation = (PickUpLocation) naverMapGeocode.getLocation(locationResult.getPickUpResult(), pickUpAddress, PickUpLocation::create);
        DropLocation dropLocation = (DropLocation) naverMapGeocode.getLocation(locationResult.getDropResult(), dropAddress, DropLocation::create);
        stopOver.updateDeliveryAddress(new DeliveryAddress(pickUpLocation, dropLocation));
    }

}
