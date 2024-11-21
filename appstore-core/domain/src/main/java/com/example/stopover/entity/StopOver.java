package com.example.stopover.entity;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.entity.Delivery;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StopOver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private DeliveryAddress deliveryAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Builder
    private StopOver(Long id, DeliveryAddress deliveryAddress, Delivery delivery) {
        this.id = id;
        this.deliveryAddress = deliveryAddress;
        this.delivery = delivery;
    }

    public void updateDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void updateDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
