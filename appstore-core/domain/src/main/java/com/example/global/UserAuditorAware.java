package com.example.global;

import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal.equals("anonymousUser")) {
            return Optional.empty();
        }
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return Optional.of(userDetails.getAuthorities() + " " + userDetails.getUsername());
        } else if (principal instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauthUser = (DefaultOAuth2User) principal;
            return Optional.of(oauthUser.getAttribute("id").toString());
        }
        return Optional.empty();
    }
}
