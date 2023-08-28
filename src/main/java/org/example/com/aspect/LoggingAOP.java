package org.example.com.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAOP {

    @Around("execution(* org.example.com.controller..*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Before executing method: " + methodName);

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            System.out.println("Exception is method " + methodName + ". Exception: " + e.getMessage());;
        }finally {
            long endTime = System.currentTimeMillis();
            System.out.println("After executing method " + methodName);
            System.out.println("Method " + methodName + " took " + (endTime-startTime) + " ms");
        }
        System.out.println("After returning from method " + methodName + ". Result: " + result);
        return result;
    }
}
