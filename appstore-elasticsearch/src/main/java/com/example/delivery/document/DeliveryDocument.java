package com.example.delivery.document;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.entity.Delivery;
import com.example.driver.model.Vehicle;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Builder
@NoArgsConstructor
@Document(indexName = "delivery")
public class DeliveryDocument {

    @Id
    private Long id;

    private String reservationNumber;

    private String deliveryCategory;

    private LocalDateTime deliveryDate;

    private Vehicle vehicle;

    private DeliveryAddress deliveryAddress;

    private BigDecimal deliveryFee;

    @Builder
    public DeliveryDocument(Long id, String reservationNumber, String deliveryCategory,
        LocalDateTime deliveryDate, Vehicle vehicle, DeliveryAddress deliveryAddress,
        BigDecimal deliveryFee) {
        this.id = id;
        this.reservationNumber = reservationNumber;
        this.deliveryCategory = deliveryCategory;
        this.deliveryDate = deliveryDate;
        this.vehicle = vehicle;
        this.deliveryAddress = deliveryAddress;
        this.deliveryFee = deliveryFee;
    }

    public static DeliveryDocument from(Delivery delivery) {
        return DeliveryDocument.builder()
            .id(delivery.getId())
            .reservationNumber(delivery.getReservationNumber())
            .deliveryCategory(delivery.getDeliveryCategory().toString())
            .deliveryDate(delivery.getDeliveryDate())
            .vehicle(delivery.getVehicle())
            .deliveryAddress(delivery.getDeliveryAddress())
            .deliveryFee(delivery.getDeliveryFee())
            .build();
    }

}
