package com.example.member.eventlistener;

import static com.example.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

import com.example.member.event.MemberExistenceCheckEvent;
import com.example.member.exception.MemberException;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEventListener {

    private final MemberRepository memberRepository;

    @EventListener
    public void handleMemberExistenceCheckEvent(MemberExistenceCheckEvent memberExistenceCheckEvent) {
        Long memberId = memberExistenceCheckEvent.getMemberId();
        memberRepository.findById(memberId)
            .orElseThrow(() -> MemberException.fromErrorCode(MEMBER_NOT_FOUND));
    }

}


