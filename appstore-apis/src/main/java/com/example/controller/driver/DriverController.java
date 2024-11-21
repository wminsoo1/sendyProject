package com.example.controller.driver;

import static org.springframework.http.HttpStatus.CREATED;

import com.example.controller.aop.ValidateUser;
import com.example.delivery.model.dto.response.DeliveryDetailResponse;
import com.example.driver.model.dto.request.CreateDriverRequest;
import com.example.driver.model.dto.request.SignInDriverRequest;
import com.example.driver.service.DriverService;
import com.example.global.Roles;
import com.example.global.jwt.CustomUserDetails;
import com.example.global.jwt.JwtToken;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/driver")
@RestController
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<Void> createDriver(
        @RequestPart("driverData") String driverData,
        @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        CreateDriverRequest createDriverRequest = CreateDriverRequest.mapToCreateDriverRequest(driverData, multipartFile);

        driverService.createDriver(createDriverRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDriverRequest signInDriverRequest) {
        return driverService.signIn(signInDriverRequest);
    }

    @ValidateUser(roles = Roles.DRIVER)
    @DeleteMapping("/{driverId}")
    public void deleteDriver(@PathVariable(value = "driverId", required = true) Long driverId) {
        driverService.deleteDriver(driverId);
    }

    @ValidateUser(roles = Roles.DRIVER)
    @PostMapping("/deliveries/{reservationNumber}")
    public ResponseEntity<DeliveryDetailResponse> matchingDeliveryWithDriver(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @PathVariable(value = "reservationNumber", required = true) String reservationNumber) {
        DeliveryDetailResponse deliveryDetailResponse = driverService.matchingDeliveryWithDriver(customUserDetails.getId(), reservationNumber);

        return ResponseEntity.ok().body(deliveryDetailResponse);
    }

    @ValidateUser(roles = Roles.DRIVER)
    @PostMapping("/{reservationNumber}")
    public ResponseEntity<DeliveryDetailResponse> completedDelivery(
        @PathVariable(value = "reservationNumber", required = true) String reservationNumber) {
        DeliveryDetailResponse deliveryDetailResponse = driverService.completedDelivery(reservationNumber);

        return ResponseEntity.ok().body(deliveryDetailResponse);
    }



}
