package com.example.global.aop.filter;

import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FilterAspect {

    private final EntityManager entityManager;

    public FilterAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Around("@annotation(filterByDeletedStatus)")
    public Object applyDeletedFilter(ProceedingJoinPoint joinPoint, FilterByDeletedStatus filterByDeletedStatus) throws Throwable {
        Session session = entityManager.unwrap(Session.class);

        session.enableFilter("deletedFilter").setParameter("isDeleted", filterByDeletedStatus.value());

        try {
            return joinPoint.proceed();
        } finally {
            session.disableFilter("deletedFilter");
        }
    }
}
