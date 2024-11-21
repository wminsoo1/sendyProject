package com.example.wallet.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.TestConfiguration;
import com.example.delivery.service.DeliveryService;
import com.example.member.model.dto.request.SignUpRequest;
import com.example.member.model.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.member.service.MemberService;
import com.example.wallet.model.CardCompany;
import com.example.wallet.model.dto.request.ChargeRequest;
import com.example.wallet.model.dto.request.CreateWalletRequest;
import com.example.wallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {TestConfiguration.class})
@SpringBootTest
@EnableFeignClients(basePackages = "com.example.global.navermap") // 추가
class WalletIntegrationTest {

    @Autowired
    WalletService walletService;

    @Autowired
    MemberService memberService;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WalletRepository walletRepository;

    @DisplayName("redis lock 확인")
    @Test
    void test1() throws InterruptedException {
        Long walletId = 1L;
        SignUpRequest signUpRequest = SignUpRequest.builder()
            .memberName("member")
            .email("asd@naver.com")
            .password("1234567a!")
            .build();

        Member member = Member.builder()
            .id(1L)
            .memberName("member")
            .password("1234567a!")
            .email("asd@naber.com")
            .build();

        memberService.signUp(signUpRequest);
        walletService.createWallet(member.getId(), new CreateWalletRequest(CardCompany.SHINHAN));

        assertTrue(memberRepository.findById(member.getId()).isPresent(), "운전자가 존재해야 합니다.");
        assertTrue(walletRepository.findById(walletId).isPresent(), "지갑이 존재해야 합니다.");

        int numOfThread = 5;
        ExecutorService service = new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(numOfThread));

        for (int i = 0; i < numOfThread; i++) {
            service.submit(() -> {
                try {
                    BigDecimal charge = walletService.charge(walletId, new ChargeRequest(BigDecimal.TEN));
                    System.out.println("충전에 성공하였습니다 charge = " + charge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        Thread.sleep(1000);
        service.shutdown();
    }


}