package com.example.wallet.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChargeRequest {

    @NotNull(message = "충전 금액은 필수 입력 항목입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "충전 금액은 0보다 커야 합니다.")
    private BigDecimal chargeBalance;
}
