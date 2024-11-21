package com.example.delivery.model.dto.request;


import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.DeliveryCategory;
import com.example.delivery.model.entity.Delivery;
import com.example.stopover.entity.StopOver;
import com.example.driver.model.Vehicle;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliverySaveRequest {

    @Embedded
    @NotNull(message = "배송 카테고리는 필수 입력 항목입니다.")
    private DeliveryCategory deliveryCategory;

    @NotNull(message = "배송 날짜는 필수 입력 항목입니다.")
    private LocalDateTime deliveryDate;

    @Embedded
    @NotNull(message = "차량 정보는 필수 입력 항목입니다.")
    private Vehicle vehicle;

    @Embedded
    @NotNull(message = "배송 주소는 필수 입력 항목입니다.")
    private DeliveryAddress deliveryAddress;

    @Embedded
    @Nullable
    private List<DeliveryAddress> stopOverAddresses;

    @NotNull(message = "배송 옵션은 필수 입력 항목입니다.")
    private String deliveryOptions;

    public DeliverySaveRequest(DeliveryCategory deliveryCategory, LocalDateTime deliveryDate, Vehicle vehicle, DeliveryAddress deliveryAddress, @Nullable List<DeliveryAddress> stopOverAddresses, String deliveryOptions) {
        this.deliveryCategory = deliveryCategory;
        this.deliveryDate = deliveryDate;
        this.vehicle = vehicle;
        this.deliveryAddress = deliveryAddress;
        this.stopOverAddresses = stopOverAddresses;
        this.deliveryOptions = deliveryOptions;
    }

    public Delivery toDelivery() {
        return Delivery.builder()
                .deliveryCategory(deliveryCategory)
                .deliveryDate(deliveryDate)
                .vehicle(vehicle)
                .deliveryAddress(deliveryAddress)
                .deliveryOptions(deliveryOptions)
                .build();
    }

    public List<StopOver> toStopOvers() {
        return stopOverAddresses.stream()
                .map(address -> StopOver.builder()
                        .deliveryAddress(address)
                        .build())
                .toList();
    }
}
