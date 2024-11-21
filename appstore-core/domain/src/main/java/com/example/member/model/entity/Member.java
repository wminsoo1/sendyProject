package com.example.member.model.entity;

import com.example.global.BaseEntity;
import com.example.global.oauth2.OAuthProvider;
import com.example.global.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberName;

    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Roles roles;

    private Long provideId;

    @Enumerated(value = EnumType.STRING)
    private OAuthProvider provider;

    @Builder
    private Member(Long id, String memberName, String password, String email, Roles roles,
        Long provideId, OAuthProvider provider) {
        this.id = id;
        this.memberName = memberName;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.provideId = provideId;
        this.provider = provider;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRoles(Roles roles) {
        this.roles = roles;
    }

    public void assignUserRole() {
        this.roles = Roles.USER;
    }

    public void assignAdminRole() {
        this.roles = Roles.ADMIN;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(roles.name()));
    }

    @Override
    public String getUsername() {
        return memberName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
