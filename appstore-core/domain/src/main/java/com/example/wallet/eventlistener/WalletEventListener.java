package com.example.wallet.eventlistener;

import com.example.wallet.event.WalletDeletedEvent;
import com.example.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletEventListener {

    private final WalletService walletService;

    @EventListener
    public void handleWalletDeletedEvent(WalletDeletedEvent walletDeletedEvent) {
        walletService.deleteWallets(walletDeletedEvent.getMemberId());
    }
}
