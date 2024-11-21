package com.example.wallet.model;

import lombok.Getter;

@Getter
public enum CardCompany {
    SHINHAN("신한"),
    KB("국민"),
    IBK("기업"),
    WOORI("우리"),
    HANA("하나"),
    SAMSUNG("삼성"),
    LOTTE("롯데"),
    NH("농협");

    private final String description;

    CardCompany(String description) {
        this.description = description;
    }
    }
