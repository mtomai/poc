package it.example.crud.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//import static org.springframework.boot.env.ConfigTreePropertySource.PropertyFile.findAll;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

        @Around("@annotation(it.example.crud.demo.permission.Logging)")
        public Object loggingExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
            long start = System.currentTimeMillis();

            long executionTime = System.currentTimeMillis() - start;

            log.info(joinPoint.getSignature() + " executed in " + executionTime + "ms");

            //serve per far andare avanti il programma
            return joinPoint.proceed() ;
        }


}
