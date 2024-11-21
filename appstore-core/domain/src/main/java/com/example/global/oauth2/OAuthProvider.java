package com.example.global.oauth2;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum OAuthProvider {
    KAKAO("카카오"),
    GOOGLE("구글");

    private final String description;

    OAuthProvider(String description) {
        this.description = description;
    }

    public static OAuthProvider getOAuthProvider(String provider) {
        return Arrays.stream(OAuthProvider.values())
            .findFirst()
            .filter(oauthProvider -> oauthProvider.name().equalsIgnoreCase(provider))
            .orElseThrow(() -> new IllegalArgumentException("Invalid provider: " + provider));
    }

}
