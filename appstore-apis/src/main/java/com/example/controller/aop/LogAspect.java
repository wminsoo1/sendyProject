package com.example.controller.aop;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {


    @Pointcut("execution(* com.example.controller..*.*(..))")
    public void controllerAdvice() {
    }

    @Before("controllerAdvice()")
    public void requestLogging() {
        MDC.put("traceId", UUID.randomUUID().toString());
    }

    @AfterReturning(pointcut = "controllerAdvice()")
    public void responseLogging() {
        MDC.clear();
    }
}
