package com.example.member.repository;


import com.example.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberName(String memberName);

    Optional<Member> findByProvideId(Long provideId);

}
