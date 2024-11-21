package com.example.wallet.model.entity;

import com.example.global.BaseEntity;
import com.example.wallet.model.CardCompany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "charge_idempotency_key", unique = true)
    private String chargeIdempotencyKey;

    @Column(name = "payment_idempotency_key", unique = true)
    private String paymentIdempotencyKey;

    @Enumerated(value = EnumType.STRING)
    private CardCompany cardCompany;

    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder
    private Wallet(Long id, Long memberId, String chargeIdempotencyKey, String paymentIdempotencyKey,
        CardCompany cardCompany, BigDecimal balance) {
        this.id = id;
        this.memberId = memberId;
        this.chargeIdempotencyKey = chargeIdempotencyKey;
        this.paymentIdempotencyKey = paymentIdempotencyKey;
        this.cardCompany = cardCompany;
        this.balance = balance;
    }

    public void updateMember(Long memberId) {
        this.memberId = memberId;
    }

    public void increaseBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void decreaseBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public void updateChargeIdempotencyKey(String chargeIdempotencyKey) {
        this.chargeIdempotencyKey = chargeIdempotencyKey;
    }

    public void updatePaymentIdempotencyKey(String paymentIdempotencyKey) {
        this.paymentIdempotencyKey = paymentIdempotencyKey;
    }

    public String generateChargeKey(Long walletId, BigDecimal chargeBalance) {
        return walletId + "-" + chargeBalance + "-" + UUID.randomUUID();
    }

    public String generatePaymentKey(Long walletId, String reservationNumber) {
        return walletId + "-" + reservationNumber + "-" + UUID.randomUUID();
    }


}
