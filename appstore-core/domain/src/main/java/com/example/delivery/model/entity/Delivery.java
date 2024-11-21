package com.example.delivery.model.entity;

import com.example.completeddelivery.entity.CompletedDelivery;
import com.example.deleteddelivery.entity.DeletedDelivery;
import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.DeliveryCategory;
import com.example.delivery.model.DeliveryStatus;
import com.example.delivery.model.dto.request.DeliveryUpdateRequest;
import com.example.driver.model.Vehicle;
import com.example.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Builder
@SQLDelete(sql = "UPDATE delivery SET deleted = 'T' WHERE id = ?") //soft delete 적용 soft delete 또한 competedDelivery와 같이 다른 테이블에 만드는게 좋을수도? 그럼 soft delete 또한 필요한가?
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = String.class))
@Filter(name = "deletedFilter", condition = "deleted = :isDeleted")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
        indexes = {
                @Index(name = "idx_member_delivery_status", columnList = "member_id, delivery_status")
        }
)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    @Builder.Default
    @Column(name = "deleted")
    private String deleted = "F";

    @Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_status", length = 50)
    private DeliveryStatus deliveryStatus; //결제 추가하면 상태도 추가 해줘야 함

    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String reservationNumber;

    @Embedded
    @Column(nullable = false)
    private DeliveryCategory deliveryCategory;

    @Column(nullable = false)
    private LocalDateTime deliveryDate;

    @Embedded
    @Column(nullable = false)
    private Vehicle vehicle;

    @Embedded
    @Column(nullable = false)
    private DeliveryAddress deliveryAddress;

    @Column(nullable = false)
    private BigDecimal deliveryFee; //BigDemical 프론트에서 처리하는거 같음

    private String deliveryOptions;

    @Builder
    private Delivery(Long id, Long memberId, Long driverId, String idempotencyKey, String deleted,
        DeliveryStatus deliveryStatus, String reservationNumber, DeliveryCategory deliveryCategory,
        LocalDateTime deliveryDate, Vehicle vehicle, DeliveryAddress deliveryAddress,
        BigDecimal deliveryFee, String deliveryOptions) {
        this.id = id;
        this.memberId = memberId;
        this.driverId = driverId;
        this.idempotencyKey = idempotencyKey;
        this.deleted = deleted;
        this.deliveryStatus = deliveryStatus;
        this.reservationNumber = reservationNumber;
        this.deliveryCategory = deliveryCategory;
        this.deliveryDate = deliveryDate;
        this.vehicle = vehicle;
        this.deliveryAddress = deliveryAddress;
        this.deliveryFee = deliveryFee;
        this.deliveryOptions = deliveryOptions;
    }

    public CompletedDelivery toCompletedDelivery(Delivery delivery) {
        return CompletedDelivery.builder()
            .memberId(delivery.getMemberId())
            .driverId(delivery.getDriverId())
            .idempotencyKey(delivery.getIdempotencyKey())
            .deliveryStatus(delivery.getDeliveryStatus())
            .reservationNumber(delivery.getReservationNumber())
            .deliveryCategory(delivery.getDeliveryCategory())
            .deliveryDate(delivery.getDeliveryDate())
            .vehicle(delivery.getVehicle())
            .deliveryAddress(delivery.getDeliveryAddress())
            .deliveryFee(delivery.getDeliveryFee())
            .deliveryOptions(delivery.getDeliveryOptions())
            .build();
    }

    public static List<DeletedDelivery> toDeletedDeliveries(List<Delivery> deliveries) {
        return deliveries.stream()
            .map(DeletedDelivery::toDelivery)
            .toList();
    }

    public void updateMember(Long memberId) {
        this.memberId = memberId;
    }

    public void matchDriver(Long driverId) {
        this.driverId = driverId;
    }

    public void updateDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public void updateIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public void updateStatusToPaymentPending() {
        this.deliveryStatus = DeliveryStatus.PAYMENT_PENDING;
    }

    public void updateStatusToAssignmentCompleted() {
        this.deliveryStatus = DeliveryStatus.ASSIGNMENT_COMPLETED;
    }

    public void updateStatusToAssignmentPending() {
        this.deliveryStatus = DeliveryStatus.ASSIGNMENT_PENDING;
    }

    public void updateStatusToDeliveryCompleted() {
        this.deliveryStatus = DeliveryStatus.DELIVERY_COMPLETED;
    }

    public void updateReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void updateDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void update(DeliveryUpdateRequest deliveryUpdateRequest) {
        this.deliveryAddress = deliveryAddress.update(deliveryUpdateRequest.getDeliveryAddress());
        this.vehicle = vehicle.update(deliveryUpdateRequest.getVehicle());
        this.deliveryDate = deliveryUpdateRequest.getDeliveryDate();
        this.deliveryOptions = deliveryUpdateRequest.getDeliveryOptions();
    }
    
    public String generateMatchIdempotencyKey(Long driverId) {
        return reservationNumber + driverId;
    }

    @Override
    public String toString() {
        return "Delivery{" +
            "id=" + id +
            ", memberId=" + memberId +
            ", driverId=" + driverId +
            ", deliveryStatus=" + deliveryStatus +
            ", reservationNumber='" + reservationNumber + '\'' +
            ", deliveryCategory=" + deliveryCategory +
            ", deliveryDate=" + deliveryDate +
            ", vehicle=" + vehicle +
            ", deliveryAddress=" + deliveryAddress +
            ", deliveryFee=" + deliveryFee +
            ", deliveryOptions='" + deliveryOptions + '\'' +
            '}';
    }

}
