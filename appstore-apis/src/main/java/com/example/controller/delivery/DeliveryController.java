package com.example.controller.delivery;

import static org.springframework.http.HttpStatus.CREATED;

import com.example.controller.aop.ValidateUser;
import com.example.delivery.model.dto.request.DeliverySaveRequest;
import com.example.delivery.model.dto.request.DeliveryUpdateRequest;
import com.example.delivery.model.dto.response.DeliveryDetailResponse;
import com.example.delivery.model.dto.response.DeliverySaveResponse;
import com.example.delivery.model.dto.response.DeliverySummaryResponse;
import com.example.delivery.service.DeliveryService;
import com.example.global.Roles;
import com.example.global.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/delivery")
@RestController
@Validated
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @ValidateUser(roles = Roles.USER)
    @PostMapping
    public ResponseEntity<DeliverySaveResponse> saveDelivery(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody DeliverySaveRequest deliverySaveRequest) {
        DeliverySaveResponse deliverySaveResponse = deliveryService.saveDelivery(deliverySaveRequest, customUserDetails.getId());

        return ResponseEntity.status(CREATED).body(deliverySaveResponse);
    }

    @ValidateUser(roles = Roles.USER)
    @PutMapping("/{reservationNumber}")
    public ResponseEntity<DeliveryDetailResponse> updateDelivery(
        @PathVariable(value = "reservationNumber", required = true) String reservationNumber,
        @Valid @RequestBody DeliveryUpdateRequest deliveryUpdateRequest) {
        DeliveryDetailResponse deliveryDetailResponse = deliveryService.updateDelivery(reservationNumber, deliveryUpdateRequest);

        return ResponseEntity.ok().body(deliveryDetailResponse);
    }

    @ValidateUser(roles = Roles.USER)
    @DeleteMapping("/{reservationNumber}")
    public ResponseEntity<Void> deleteDelivery(
        @PathVariable(value = "reservationNumber", required = true) String reservationNumber) {
        deliveryService.deleteDelivery(reservationNumber);

        return ResponseEntity.noContent().build();
    }

    @ValidateUser(roles = Roles.USER)
    @GetMapping("/member/{reservationNumber}")
    public ResponseEntity<DeliveryDetailResponse> getDeliveryDetailForMember(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(value = "reservationNumber", required = true) String reservationNumber) {
        final DeliveryDetailResponse deliveryDetail = deliveryService.getDeliveryDetail(
            reservationNumber, customUserDetails.getId());

        return ResponseEntity.ok().body(deliveryDetail);
    }

    @ValidateUser(roles = Roles.DRIVER)
    @GetMapping("/driver/{reservationNumber}")
    public ResponseEntity<DeliveryDetailResponse> getDeliveryDetailForDriver(
        @PathVariable(value = "reservationNumber", required = true) String reservationNumber) {
        final DeliveryDetailResponse deliveryDetail = deliveryService.getDeliveryDetailForDriver(reservationNumber);

        return ResponseEntity.ok().body(deliveryDetail);
    }

    @ValidateUser(roles = Roles.USER)
    @GetMapping("/member")
    public ResponseEntity<List<DeliverySummaryResponse>> getDeliverySummaryResponseForMember(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam(value = "deliveryId", required = false) Long deliveryId,
        @RequestParam(value = "pageSize", required = true) @Max(value = 100, message = "pageSize의 최댓값은 100입니다") int pageSize) {
        final List<DeliverySummaryResponse> deliveriesSummary = deliveryService.getDeliverySummaryResponseForMember(
            customUserDetails.getId(), deliveryId, pageSize);

        return ResponseEntity.ok().body(deliveriesSummary);
    }

    @ValidateUser(roles = Roles.DRIVER)
    @GetMapping("/driver")
    public ResponseEntity<List<DeliverySummaryResponse>> getDeliverySummaryResponseForDriver(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestParam(value = "deliveryId", required = false) Long deliveryId,
        @RequestParam(value = "driverLatitude", required = true) Double driverLatitude,
        @RequestParam(value = "driverLongitude", required = true) Double driverLongitude,
        @RequestParam(value = "pageSize", required = true) @Max(value = 100, message = "pageSize의 최댓값은 100입니다") int pageSize) {
        final List<DeliverySummaryResponse> deliveriesSummary = deliveryService.getDeliverySummaryResponseForDriver(
            customUserDetails.getId(), deliveryId, driverLatitude, driverLongitude, pageSize);

        return ResponseEntity.ok().body(deliveriesSummary);
    }

    /*
    @GetMapping("/deliverys/cache")
    public ResponseEntity<List<DeliverySummaryResponse>> getDeliverySummariesWithCache(
            @RequestHeader("memberId") Long memberId) {
        final List<DeliverySummaryResponse> deliveriesSummary = deliveryService.getDeliverySummariesWithCache(memberId);

        return ResponseEntity.ok().body(deliveriesSummary);
    }
    */
}
