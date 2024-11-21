package com.example.wallet.repository;


import com.example.wallet.model.entity.Wallet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findByMemberId(Long memberId);

    boolean existsByChargeIdempotencyKey(String chargeIdempotencyKey);

    boolean existsByPaymentIdempotencyKey(String paymentIdempotencyKey);
}
