package org.api.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

//    @Around("execution(* org.api.repository..*(..))")
//    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startTime = System.currentTimeMillis();
//        Object result = joinPoint.proceed(); // 메소드 실행
//        long endTime = System.currentTimeMillis();
//
//        // 실행 시간 계산 및 로그 출력
//        System.out.println(joinPoint.getSignature() + " executed in " + (endTime - startTime) + "ms");
//        return result;
//    }

    @Around("execution(* org.api.service.FileUploadService.uploadFiles(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 메소드 실행
        long endTime = System.currentTimeMillis();

        // 실행 시간 계산 및 로그 출력
        System.out.println(joinPoint.getSignature() + " executed in " + (endTime - startTime) + "ms");
        return result;
    }


}
