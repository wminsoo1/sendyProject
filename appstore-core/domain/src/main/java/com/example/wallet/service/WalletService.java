package com.example.wallet.service;

import static com.example.wallet.exception.WalletErrorCode.EXCEEDED_MAX_WALLET_COUNT;
import static com.example.wallet.exception.WalletErrorCode.INSUFFICIENT_BALANCE;
import static com.example.wallet.exception.WalletErrorCode.NEGATIVE_OR_ZERO_CHARGE_AMOUNT;
import static com.example.wallet.exception.WalletErrorCode.NEGATIVE_OR_ZERO_PAYMENT_AMOUNT;
import static com.example.wallet.exception.WalletErrorCode.WALLET_NOT_FOUND;

import com.example.delivery.exception.DeliveryErrorCode;
import com.example.delivery.exception.DeliveryException;
import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.entity.Delivery;
import com.example.delivery.repository.DeliveryRepository;
import com.example.global.aop.lock.RedissonLock;
import com.example.member.event.MemberExistenceCheckEvent;
import com.example.stopover.entity.StopOver;
import com.example.stopover.repository.StopOverRepository;
import com.example.wallet.exception.WalletException;
import com.example.wallet.model.dto.request.ChargeRequest;
import com.example.wallet.model.dto.request.CreateWalletRequest;
import com.example.wallet.model.dto.response.PaymentResponse;
import com.example.wallet.model.entity.Wallet;
import com.example.wallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletService {

    private static final int MAX_WALLET_COUNT = 3; //카드 개수 최대 3개

    private final WalletRepository walletRepository;
    private final DeliveryRepository deliveryRepository;
    private final StopOverRepository stopOverRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createWallet(Long memberId, CreateWalletRequest createWalletRequest) {
        eventPublisher.publishEvent(new MemberExistenceCheckEvent(memberId));
        validateWalletMaxCount(memberId);

        Wallet wallet = CreateWalletRequest.toWallet(createWalletRequest);
        wallet.updateMember(memberId);
        walletRepository.save(wallet);
    }

    @Transactional
    public void deleteWallet(Long memberId) {
        Wallet wallet = walletRepository.findById(memberId)
            .orElseThrow(() -> WalletException.fromErrorCode(WALLET_NOT_FOUND));

        walletRepository.delete(wallet);
    }

    @Transactional
    public void deleteWallets(Long memberId) {
        List<Wallet> wallets = walletRepository.findByMemberId(memberId);
        if (CollectionUtils.isEmpty(wallets)) {
            return;
        }

        walletRepository.deleteAll(wallets);
    }

    @RedissonLock(value = "#walletId")
    @Transactional
    public BigDecimal charge(Long walletId, ChargeRequest chargeRequest) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletException(WALLET_NOT_FOUND));

        BigDecimal chargeBalance = chargeRequest.getChargeBalance();
        if (chargeBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException(NEGATIVE_OR_ZERO_CHARGE_AMOUNT);
        }

        String chargeIdempotencyKey = wallet.generateChargeKey(walletId, chargeRequest.getChargeBalance());
        if (walletRepository.existsByChargeIdempotencyKey(chargeIdempotencyKey)) {
            return wallet.getBalance();
        }

        wallet.updateChargeIdempotencyKey(chargeIdempotencyKey);
        wallet.increaseBalance(chargeBalance);

        return wallet.getBalance();
    }

    @RedissonLock(value = "#walletId")
    @Transactional
    public PaymentResponse payment(Long walletId, String reservationNumber) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletException(WALLET_NOT_FOUND));

        Delivery delivery = deliveryRepository.findByReservationNumber(reservationNumber)
            .orElseThrow(() -> new DeliveryException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        BigDecimal paymentBalance = delivery.getDeliveryFee();
        if (paymentBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException(NEGATIVE_OR_ZERO_PAYMENT_AMOUNT);
        }

        if (wallet.getBalance().compareTo(paymentBalance) < 0) {
            throw new WalletException(INSUFFICIENT_BALANCE);
        }

        List<StopOver> stopOvers = stopOverRepository.findStopOverByReservationNumber(reservationNumber);

        String paymentIdempotencyKey = wallet.generatePaymentKey(walletId, reservationNumber);
        if (walletRepository.existsByPaymentIdempotencyKey(paymentIdempotencyKey)) {
            return getPaymentResponse(stopOvers, paymentBalance, wallet, delivery);
        }

        wallet.updatePaymentIdempotencyKey(paymentIdempotencyKey);
        wallet.decreaseBalance(paymentBalance);
        delivery.updateStatusToAssignmentPending();

        return getPaymentResponse(stopOvers, paymentBalance, wallet, delivery);
    }

    private PaymentResponse getPaymentResponse(List<StopOver> stopOvers, BigDecimal paymentBalance, Wallet wallet, Delivery delivery) {
        if (CollectionUtils.isEmpty(stopOvers)) {
            return PaymentResponse.from(paymentBalance, wallet, delivery, Collections.emptyList());
        }

        List<DeliveryAddress> deliveryAddresses = extractDeliveryAddresses(stopOvers);

        return PaymentResponse.from(paymentBalance, wallet, delivery, deliveryAddresses);
    }

    private void validateWalletMaxCount(Long memberId) {
        if (walletRepository.findByMemberId(memberId).size() > MAX_WALLET_COUNT) {
            throw new WalletException(EXCEEDED_MAX_WALLET_COUNT);
        }
    }

    private List<DeliveryAddress> extractDeliveryAddresses(List<StopOver> stopOvers) {
        return stopOvers.stream()
            .map(StopOver::getDeliveryAddress)
            .toList();
    }

}
