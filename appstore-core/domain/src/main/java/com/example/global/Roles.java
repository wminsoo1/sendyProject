package com.example.global;

import lombok.Getter;

@Getter
public enum Roles {

    USER("사용자"),
    DRIVER("운전자"),
    ADMIN("관리자");

    private final String description;

    Roles(String description) {
        this.description = description;
    }
}
