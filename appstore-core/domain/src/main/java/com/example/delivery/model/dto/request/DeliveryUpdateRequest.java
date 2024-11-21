package com.example.delivery.model.dto.request;


import com.example.delivery.model.DeliveryAddress;
import com.example.driver.model.Vehicle;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryUpdateRequest {

    @Embedded
    @NotNull(message = "배송 주소는 필수 입력 항목입니다.")
    private DeliveryAddress deliveryAddress;

    @Embedded
    @NotNull(message = "차량 정보는 필수 입력 항목입니다.")
    private Vehicle vehicle;

    @NotNull(message = "배송 날짜는 필수 입력 항목입니다.")
    private LocalDateTime deliveryDate;

    @NotNull(message = "배송 옵션은 필수 입력 항목입니다.")
    private String deliveryOptions;

    @Embedded
    @Nullable
    private List<DeliveryAddress> stopOverAddresses;

    public DeliveryUpdateRequest(DeliveryAddress deliveryAddress, Vehicle vehicle, LocalDateTime deliveryDate, String deliveryOptions, @Nullable List<DeliveryAddress> stopOverAddresses) {
        this.deliveryAddress = deliveryAddress;
        this.vehicle = vehicle;
        this.deliveryDate = deliveryDate;
        this.deliveryOptions = deliveryOptions;
        this.stopOverAddresses = stopOverAddresses;
    }
}
