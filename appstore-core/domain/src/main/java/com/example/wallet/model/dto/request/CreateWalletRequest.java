package com.example.wallet.model.dto.request;

import com.example.wallet.model.CardCompany;
import com.example.wallet.model.entity.Wallet;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateWalletRequest {

    @NotNull(message = "카드사는 필수 입력 항목입니다.")
    private CardCompany cardCompany;

    public static Wallet toWallet(CreateWalletRequest createWalletRequest) {
        return Wallet.builder()
                .cardCompany(createWalletRequest.cardCompany)
                .build();
    }
}
