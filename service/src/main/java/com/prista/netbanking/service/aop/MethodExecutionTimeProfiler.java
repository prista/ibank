package com.prista.netbanking.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class MethodExecutionTimeProfiler {

    private static final Logger LOGGER = LoggerFactory.getLogger("MethodExecutionTimeProfiler");

    @Pointcut("execution(* com.prista.netbanking.service.impl.*.*(..))")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.nanoTime();
        Object output = pjp.proceed();
        long executionTime = System.nanoTime() - start;
        LOGGER.debug("method execution: [{}:{}={} microsecond]", pjp.getTarget().getClass().getSimpleName(), pjp.getSignature().getName(), executionTime / 1000);
        return output;
    }

}