package org.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class LoggingAspect {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {}

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void servicePointcut() {}

    @Around("controllerPointcut() || servicePointcut()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String timestamp = LocalTime.now().format(TIME_FORMATTER);
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        // Форматируем параметры
        StringBuilder params = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) params.append(", ");
            params.append(args[i]);
        }

        // Выполняем метод
        Object result = joinPoint.proceed();

        // Формируем лог
        System.out.printf("[%s] %s.%s(), параметры: [%s], возврат: %s%n",
                timestamp,
                className,
                methodName,
                params,
                result != null ? result.toString() : "void");

        return result;
    }
}