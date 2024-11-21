package com.example.global.user;

import com.example.driver.model.entity.Driver;
import com.example.driver.repository.DriverRepository;
import com.example.global.jwt.CustomUserDetails;
import com.example.member.model.entity.Member;
import com.example.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<CustomUserDetails> member = memberRepository.findByMemberName(username)
            .map(this::createUserDetails);

        // 드라이버로부터 사용자 정보 조회
        Optional<CustomUserDetails> driver = driverRepository.findByDriverName(username)
            .map(this::createUserDetails);

        // 멤버 또는 드라이버가 존재할 경우 반환
        if (member.isPresent()) {
            return member.get();
        } else if (driver.isPresent()) {
            return driver.get();
        }

        throw new RuntimeException("사용자가 없습니다");
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private CustomUserDetails createUserDetails(Member member) {
        return new CustomUserDetails(
            (User) User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .authorities(member.getAuthorities())
                .build(),
            member.getId()
        );
    }

    private CustomUserDetails createUserDetails(Driver driver) {
        return new CustomUserDetails(
            (User) User.builder()
                .username(driver.getUsername())
                .password(driver.getPassword())
                .authorities(driver.getAuthorities())
                .build(),
            driver.getId()
        );
    }
}
