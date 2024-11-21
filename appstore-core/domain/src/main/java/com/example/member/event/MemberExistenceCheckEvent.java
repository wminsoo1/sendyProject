package com.example.member.event;

import lombok.Getter;

@Getter
public class MemberExistenceCheckEvent {

    private final Long memberId;

    public MemberExistenceCheckEvent(Long memberId) {
        this.memberId = memberId;
    }
}
