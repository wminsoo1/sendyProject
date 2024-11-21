package com.example.wallet.event;

import lombok.Getter;

@Getter
public class WalletDeletedEvent {

    private final Long memberId;

    public WalletDeletedEvent(Long memberId) {
        this.memberId = memberId;
    }
}
