package com.example.controller.global.filter;

import com.example.global.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    // 인증이 필요 없는 URL 목록
    private final List<String> EXCLUDE_URLS = List.of(
        "/api/member/sign-in",
        "/api/member/sign-up",
        "/api/driver",
        "/api/driver/sign-in",
        "/login-oauth2"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest  = (HttpServletRequest) request;

        // 인증이 필요 없는 URL은 필터링하지 않고 다음 필터로 이동
        if (EXCLUDE_URLS.contains(httpRequest.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = resolveToken(httpRequest);

            if (token == null) {
                throw new RuntimeException("토큰이 없습니다");
            }

            // 2. validateToken으로 토큰 유효성 검사
            if (jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } catch (RuntimeException ex) {
            // 필터 단계에서 발생한 예외 처리
            handleAuthenticationException((HttpServletResponse) response, ex);
        }
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void handleAuthenticationException(HttpServletResponse response, RuntimeException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
    }
}
