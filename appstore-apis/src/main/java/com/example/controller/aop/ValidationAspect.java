package com.example.controller.aop;

import static com.example.controller.exception.AccessErrorCode.DRIVER_ACCESS;
import static com.example.controller.exception.AccessErrorCode.UNAUTHORIZED_ACCESS;
import static com.example.controller.exception.AccessErrorCode.USER_ACCESS;

import com.example.controller.exception.AccessException;
import com.example.global.Roles;
import com.example.global.jwt.CustomUserDetails;
import java.lang.reflect.Method;
import java.util.Collection;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidationAspect {

    @Before("@annotation(com.example.controller.aop.ValidateUser)")
    public void validateUserAccess(JoinPoint joinPoint) {
        CustomUserDetails customUserDetails = getAuthenticatedUser();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        ValidateUser validateUser = method.getAnnotation(ValidateUser.class);
        Roles requiredRole = validateUser.roles();

        validateAuthenticatedUser(customUserDetails);
        if (requiredRole == Roles.DRIVER) {
            validateDriverRoles(customUserDetails.getAuthorities());
        } else {
            validateUserRoles(customUserDetails.getAuthorities());
        }
    }

    private CustomUserDetails getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        throw new AccessException(UNAUTHORIZED_ACCESS);
    }

    private void validateAuthenticatedUser(CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            throw new AccessException(UNAUTHORIZED_ACCESS);
        }
    }

    private void validateUserRoles(Collection<? extends GrantedAuthority> authorities) {
        boolean isAuthorized = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals(Roles.ADMIN.name()) || role.equals(Roles.USER.name()));

        if (!isAuthorized) {
            throw new AccessException(USER_ACCESS);
        }
    }

    private void validateDriverRoles(Collection<? extends GrantedAuthority> authorities) {
        boolean isAuthorized = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals(Roles.ADMIN.name()) || role.equals(Roles.DRIVER.name()));

        if (!isAuthorized) {
            throw new AccessException(DRIVER_ACCESS);
        }
    }

}
