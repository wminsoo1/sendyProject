package com.example.driver.model.entity;

import com.example.driver.model.Vehicle;
import com.example.global.BaseEntity;
import com.example.global.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
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
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Driver extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String driverName;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Vehicle vehicle;

    @Enumerated(value = EnumType.STRING)
    private Roles roles;

    @Column(nullable = true) //운전자 생성 시 aws s3를 추가해 이미지 url을 받아올 예정
    private String imageUrl;

    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Builder
    private Driver(Long id, String driverName, String password, Vehicle vehicle, Roles roles,
        String imageUrl, BigDecimal balance) {
        this.id = id;
        this.driverName = driverName;
        this.password = password;
        this.vehicle = vehicle;
        this.roles = roles;
        this.imageUrl = imageUrl;
        this.balance = balance;
    }

    public void increaseBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void assignDriverRole() {
        this.roles = Roles.DRIVER;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(roles.name()));
    }

    @Override
    public String getUsername() {
        return driverName;
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

    @Override
    public String toString() {
        return "Driver{" +
            "id=" + id +
            ", driverName='" + driverName + '\'' +
            ", password='" + password + '\'' +
            ", vehicle=" + vehicle +
            ", roles=" + roles +
            '}';
    }
}

