package com.example.delivery.service;

import com.example.delivery.model.dto.response.DeliverySummaryResponse;
import java.util.List;

public interface DeliveryServiceCustom {

    List<DeliverySummaryResponse> getDeliverySummaryResponseForMember(Long memberId, Long deliveryId, int pageSize);

    List<DeliverySummaryResponse> getDeliverySummaryResponseForDriver(Long driverId, Long deliveryId, Double driverLatitude, Double driverLongitude, int pageSize);
}
