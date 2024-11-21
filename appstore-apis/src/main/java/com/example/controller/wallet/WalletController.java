package com.example.controller.wallet;

import static org.springframework.http.HttpStatus.CREATED;

import com.example.controller.aop.ValidateUser;
import com.example.global.Roles;
import com.example.global.jwt.CustomUserDetails;
import com.example.wallet.model.dto.request.ChargeRequest;
import com.example.wallet.model.dto.request.CreateWalletRequest;
import com.example.wallet.model.dto.response.PaymentResponse;
import com.example.wallet.service.WalletService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor()
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    @ValidateUser(roles = Roles.USER)
    @PostMapping
    public ResponseEntity<Void> createWallet(
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody CreateWalletRequest createWalletRequest) {
        walletService.createWallet(customUserDetails.getId(), createWalletRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @ValidateUser(roles = Roles.USER)
    @DeleteMapping("/{walletId}")
    public void deleteWallet(@PathVariable(value = "walletId", required = true) Long walletId) {
        walletService.deleteWallet(walletId);
    }

    @ValidateUser(roles = Roles.USER)
    @PostMapping("/{walletId}")
    public BigDecimal charge(
        @PathVariable(value = "walletId", required = true) Long walletId,
        @Valid @RequestBody ChargeRequest chargeRequest) {

        return walletService.charge(walletId, chargeRequest);
    }

    @ValidateUser(roles = Roles.USER)
    @PostMapping("/{walletId}/{reservationNumber}")
    public PaymentResponse payment(
        @PathVariable(value = "walletId", required = true) Long walletId,
        @PathVariable(value = "reservationNumber", required = true) String reservationNumber) {

        return walletService.payment(walletId, reservationNumber);
    }
}
