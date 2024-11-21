package com.example.delivery.service;

import static com.example.delivery.exception.DeliveryErrorCode.DELIVERY_NOT_FOUND;
import static com.example.delivery.model.DeliveryStatus.ASSIGNMENT_PENDING;
import static com.example.delivery.model.entity.QDelivery.delivery;
import static com.example.driver.exception.DriverErrorCode.DRIVER_NOT_FOUND;
import static com.example.member.model.entity.QMember.member;

import com.example.delivery.exception.DeliveryException;
import com.example.delivery.model.DeliveryStatus;
import com.example.delivery.model.dto.response.DeliverySummaryResponse;
import com.example.delivery.model.entity.Delivery;
import com.example.driver.exception.DriverException;
import com.example.driver.model.VehicleType;
import com.example.driver.model.VehicleWeight;
import com.example.driver.model.entity.Driver;
import com.example.driver.repository.DriverRepository;
import com.example.member.event.MemberExistenceCheckEvent;
import com.example.stopover.entity.StopOver;
import com.example.stopover.repository.StopOverRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class DeliveryServiceCustomImpl implements DeliveryServiceCustom {

    private final JPAQueryFactory queryFactory;
    private final StopOverRepository stopOverRepository;
    private final DriverRepository driverRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public DeliveryServiceCustomImpl(EntityManager em,StopOverRepository stopOverRepository, DriverRepository driverRepository, ApplicationEventPublisher eventPublisher) {
        queryFactory = new JPAQueryFactory(em);
        this.stopOverRepository = stopOverRepository;
        this.driverRepository = driverRepository;
        this.eventPublisher = eventPublisher;
    }

    //no offset id값이 있으면 그 id부터 size만큼 가져오기
    public List<DeliverySummaryResponse> getDeliverySummaryResponseForMember(Long memberId, Long deliveryId, int pageSize) {
        validatedMemberExist(memberId);

        List<Delivery> deliveries = findDeliveriesLessThanByIdAndMemberId(memberId, deliveryId, pageSize);

        validatedDeliveriesExist(deliveries);

        final List<Long> deliveryIds = extractDeliveryIds(deliveries);
        List<StopOver> stopOvers = stopOverRepository.findStopOverByDeliveryIds(deliveryIds);

        return createDeliverySummaryResponses(stopOvers, deliveries);
    }

    public List<DeliverySummaryResponse> getDeliverySummaryResponseForDriver(Long driverId, Long deliveryId, Double driverLatitude, Double driverLongitude, int pageSize) {
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new DriverException(DRIVER_NOT_FOUND));

        List<Delivery> deliveries = findDeliveriesForDriver(deliveryId, driverLatitude, driverLongitude, pageSize, driver);

        List<Long> deliveryIds = extractDeliveryIds(deliveries);
        List<StopOver> stopOvers = stopOverRepository.findStopOverByDeliveryIds(deliveryIds);

        return createDeliverySummaryResponses(stopOvers, deliveries);
    }

    private List<Delivery> findDeliveriesForDriver(Long deliveryId, Double driverLatitude,
        Double driverLongitude, int pageSize, Driver driver) {

        return queryFactory
            .select(delivery)
            .from(delivery)
            .where(
                ltDeliveryId(deliveryId),
                deliveryStatusEq(ASSIGNMENT_PENDING),
                vehicleTypeEq(driver.getVehicle().getVehicleType()),
                vehicleWeightEq(driver.getVehicle().getVehicleWeight())
            )
            .limit(pageSize)
            .orderBy(calculateSimpleDistance(driverLatitude, driverLongitude).asc(),
                delivery.updatedAt.asc())
            .fetch();
    }

    private void validatedDeliveriesExist(List<Delivery> deliveries) {
        if (deliveries == null || deliveries.isEmpty()) {
            throw new DeliveryException(DELIVERY_NOT_FOUND);
        }
    }

    private List<Delivery> findDeliveriesLessThanByIdAndMemberId(Long memberId, Long deliveryId, int pageSize) {
        return queryFactory
            .select(delivery)
            .from(delivery)
            .leftJoin(member).on(delivery.memberId.eq(member.id))
            .fetchJoin()
            .where(
                ltDeliveryId(deliveryId),
                memberIdEq(memberId)
            )
            .orderBy(delivery.updatedAt.asc())
            .limit(pageSize)
            .fetch();
    }

    private BooleanExpression ltDeliveryId(Long deliveryId) {
        if (deliveryId == null) {
            return null;
        }

        return delivery.id.lt(deliveryId);
    }

    private void validatedMemberExist(Long memberId) {
        eventPublisher.publishEvent(new MemberExistenceCheckEvent(memberId));
    }

    private List<Long> extractDeliveryIds(List<Delivery> deliveries) {
        return deliveries.stream()
            .map(Delivery::getId)
            .toList();
    }
    
    private NumberExpression<Double> calculateSimpleDistance(Double driverLatitude, Double driverLongitude) {
        NumberExpression<Double> latitudeDiff = delivery.deliveryAddress.pickupLocation.pickUpLatitude.subtract(driverLatitude).abs();
        NumberExpression<Double> longitudeDiff = delivery.deliveryAddress.pickupLocation.pickUpLongitude.subtract(driverLongitude).abs();

        // 위도 차이 + 경도 차이의 절대값을 계산
        return latitudeDiff.add(longitudeDiff);
    }
    
    private BooleanExpression memberIdEq(Long memberId) {
        return memberId == null ? null : member.id.eq(memberId);
    }

    private BooleanExpression vehicleTypeEq(VehicleType vehicleType) {
        return vehicleType == null ? null : delivery.vehicle.vehicleType.eq(vehicleType);
    }

    private BooleanExpression deliveryStatusEq(DeliveryStatus deliveryStatus) {
        return deliveryStatus == null ? null : delivery.deliveryStatus.eq(deliveryStatus);
    }

    private BooleanExpression vehicleWeightEq(VehicleWeight vehicleWeight) {
        return vehicleWeight == null ? null : delivery.vehicle.vehicleWeight.eq(vehicleWeight);
    }

    private List<DeliverySummaryResponse> createDeliverySummaryResponses(List<StopOver> stopOvers, List<Delivery> deliveries) {
        if (stopOvers == null || stopOvers.isEmpty()) {
            return deliveries.stream()
                    .map(delivery -> DeliverySummaryResponse.from(delivery, 0))
                    .toList();
        }

        final Map<Long, List<StopOver>> stopOversByDeliveryId = stopOvers.stream()
                .collect(Collectors.groupingBy(stopOver -> stopOver.getDelivery().getId()));

        return deliveries.stream()
                .map(delivery -> {
                    int size = stopOversByDeliveryId.getOrDefault(delivery.getId(), Collections.emptyList()).size();
                    return DeliverySummaryResponse.from(delivery, size);
                })
                .toList();
    }

}
