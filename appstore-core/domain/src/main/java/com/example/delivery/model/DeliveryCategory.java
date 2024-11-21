package com.example.delivery.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Embeddable
public class DeliveryCategory {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "배송 타입은 필수 입력 항목입니다.")
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "배송 카테코리는 필수 입력 항목입니다.")
    private PersonalDeliveryCategory deliveryPersonalCategory;

    protected DeliveryCategory() {

    }

    public DeliveryCategory(DeliveryType deliveryType, PersonalDeliveryCategory deliveryPersonalCategory) {
        this.deliveryType = deliveryType;
        this.deliveryPersonalCategory = deliveryPersonalCategory;
    }
}
