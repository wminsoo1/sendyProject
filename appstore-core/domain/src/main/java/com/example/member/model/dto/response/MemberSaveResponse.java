package com.example.member.model.dto.response;

import com.example.member.model.entity.Member;
import lombok.Getter;

@Getter
public class MemberSaveResponse {

    private String name;

    private String email;

    private MemberSaveResponse() {
    }

    private MemberSaveResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static MemberSaveResponse fromMember(final Member member) {
        return new MemberSaveResponse(member.getMemberName(), member.getEmail());
    }
}
