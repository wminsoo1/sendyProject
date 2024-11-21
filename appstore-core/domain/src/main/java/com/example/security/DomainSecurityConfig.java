package com.example.security;

import com.example.global.Roles;
import com.example.global.jwt.JwtToken;
import com.example.global.jwt.JwtTokenProvider;
import com.example.global.oauth2.OAuth2UserService;
import com.example.global.oauth2.OAuthProvider;
import com.example.member.model.entity.Member;
import com.example.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class DomainSecurityConfig {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain domainFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/login-oauth2", "/login/**", "/oauth2/**", "/s3/**")
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/s3/**", "/login-oauth2").permitAll()
                .anyRequest().authenticated())
            .oauth2Login(oauth2Configurer -> oauth2Configurer
                .loginPage("/login")
                .successHandler(successHandler())
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                    .userService(oAuth2UserService)))
            .logout((logout) -> logout
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = defaultOAuth2User.getAttributes();
            // 사용자 정보 추출
            Long provideId = Long.parseLong(attributes.get("id").toString());
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String authorizedClientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

            String roles = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("UNKNOWN");

            String nickname = properties.get("nickname").toString();

            Member member = memberRepository.findByMemberName(nickname)
                .orElseGet(() -> creatMember(provideId, nickname, roles, authorizedClientRegistrationId));

            writeTokenToResponse(authentication, member, response);
        });
    }

    private void writeTokenToResponse(Authentication authentication, Member savedMember, HttpServletResponse response) throws IOException {
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, savedMember.getId());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"token\": \"" + jwtToken + "\"}");
    }

    private Member creatMember(Long provideId, String nickname, String roles, String provider) {
        Member member = Member.builder()
            .memberName(nickname)
            .provideId(provideId)
            .provider(OAuthProvider.getOAuthProvider(provider))
            .roles(Roles.valueOf(roles))
            .build();
        memberRepository.save(member);
        return member;
    }

}
