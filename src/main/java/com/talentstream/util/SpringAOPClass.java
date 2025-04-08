package com.talentstream.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Aspect
@Slf4j
public class SpringAOPClass {
	
	@Before("execution(*, com.talentstream.service.*.*(..)")
	public void LoggersBeforeService(JoinPoint joinPoint)
	{
		String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
 
        log.info("Executing Service method: " + methodName + "() in class: " + className);		
	}

    @Before("execution(* com.tekworks.rental.controller.*.*(..))")
    public void loggerBeforeController(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
 
        log.info("Executing Controller method: " + methodName + "() in class: " + className);
    }
    @After("execution(* com.tekworks.rental.service.*.*(..))")
    public void loggerAfterService(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
 
        log.info("Execution Completed Service method: " + methodName + "() in class: " + className);
    }
    @After("execution(* com.tekworks.rental.controller.*.*(..))")
    public void loggerAfterController(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
 
        log.info("Execution Completed  Controller method: " + methodName + "() in class: " + className);
    }
}








