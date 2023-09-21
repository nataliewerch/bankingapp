package org.example.com.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * An aspect class that provides method execution logging for controllers within the application.
 * It measures the execution time of methods and logs relevant information before and after method execution.
 *
 * @author Natalie Werch
 */
@Aspect
@Component
public class LoggingAOP {

    /**
     * A method that intercepts the execution of methods in controller classes and logs relevant information.
     *
     * @param joinPoint - The join point representing the method being executed.
     * @return The result of the method execution.
     */
    @Around("execution(* org.example.com.controller..*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Before executing method: " + methodName);

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            System.out.println("Exception is method " + methodName + ". Exception: " + e.getMessage());
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println("After executing method " + methodName);
            System.out.println("Method " + methodName + " took " + (endTime - startTime) + " ms");
        }
        System.out.println("After returning from method " + methodName + ". Result: " + result);
        return result;
    }
}
