package cj.software.util.spring;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * an aspect that automatically generates trace lines in the logging. For each method entry, a short description
 * of the method name is logged with a right pointing arrow head at the beginning. After that log line, all log lines
 * are indented by a couple of blanks. When the method returns, a similar description is logged with a left pointing
 * arrow head at the beginning, and the indentation is reduced. With that behaviour, it can easily be determined in
 * which method an own log entry was produced, and which method was invoked from which other method.
 * <br/>
 * Imagine you have a class like that:
 * <pre>
 *     {@code
 *     @Service
 *     public class OuterClass {
 *         private Logger logger = LogManager.getFormatterLogger();
 *
 *         @Autowired
 *         InnerClass innerClass;
 *
 *         public void method1() {
 *             logger.info("custom log entry 1");
 *             innerClass.method2();
 *         }
 *     }
 *     }
 * </pre>
 * <p>
 * and a class like that:
 * <pre>
 *     {@code
 *     @Service
 *     public class InnerClass {
 *         private Logger logger = LogManager.getFormatterLogger();
 *         public void method2() {
 *             logger.info("log entry from inner class");
 *         }
 *     }
 *     }
 * </pre>
 * <p>
 * then the logs would look like that:
 * <pre>
 *     &gt;OuterClass.method1()
 *         custom log entry 1
 *         &gt; InnerClass.method2()
 *             log entry from inner class
 *         &lt; InnerClass.method2()
 *     &lt;OuterClass.method1()
 * </pre>
 */
@Component
@Aspect
@Order(0)
public class TraceAspect {
    private static final String KEY = "indent";
    private static final String INDENT = "    ";

    @Autowired
    private MethodDescriptionGenerator descriptionGenerator;

    @Pointcut("execution(* cj.software.genetics.schedule..*.*(..)) && ! within(TraceAspect)")
    public void eonMethod() {
        // pointcuts are empty
    }

    @Pointcut("execution(* cj.software..entity..*.*(..))")
    public void entity() {
        // pointcuts are empty
    }

    @Pointcut("execution(* cj.software..*.get*())")
    public void objectGetter() {
        // pointcuts are empty
    }

    @Pointcut("execution(boolean cj.software..*.is*())")
    public void booleanGetter() {
        // pointcuts are empty
    }

    @Pointcut("execution(* cj.software.genetics.schedule.api..*.*(..))")
    public void api() {
        // pointcuts are empty
    }

    @Pointcut("eonMethod() && ! entity() && ! objectGetter() && ! booleanGetter() && ! api()")
    public void toBeTraced() {
        // pointcuts are empty
    }

    @Pointcut("@annotation(cj.software.util.spring.Trace)")
    public void annotatedMethod() {
        // any method that has this annotation
    }

    @Before("toBeTraced()")
    public void reportMethodEntry(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Class<?> declaringType = methodSignature.getDeclaringType();
        Logger logger = LogManager.getFormatterLogger(declaringType);
        if (canLog(method, logger)) {
            Object[] args = joinPoint.getArgs();
            String[] parameterNames = methodSignature.getParameterNames();
            SortedSet<Integer> indexes = determineIndexesOfLoggedParameters(methodSignature);
            String description = descriptionGenerator.generateMethodEntry(method, parameterNames, args, indexes);
            logger.info(">%s", description);
        }
        String indentValue = MDC.get(KEY);
        if (indentValue == null) {
            indentValue = "";
        }
        indentValue = String.format("%s%s", INDENT, indentValue);
        MDC.put(KEY, indentValue);
    }

    private boolean canLog(Method method, Logger logger) {
        TraceAtLogLevel traceAtLogLevel = method.getAnnotation(TraceAtLogLevel.class);
        if (traceAtLogLevel == null) {
            Class<?> clazz = method.getDeclaringClass();
            traceAtLogLevel = clazz.getAnnotation(TraceAtLogLevel.class);
        }
        int requiredLevel = (traceAtLogLevel != null ? traceAtLogLevel.level().intLevel() : Level.INFO.intLevel());
        int loggerLevel = logger.getLevel().intLevel();
        boolean result = (loggerLevel >= requiredLevel);
        return result;
    }

    @AfterReturning(value = "toBeTraced() && annotatedMethod()", returning = "result")
    public void reportMethodReturnWithResult(JoinPoint joinPoint, Object result) {
        decreaseIndent();
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Method method = sig.getMethod();
        Class<?> declaringType = sig.getDeclaringType();
        Logger logger = LogManager.getFormatterLogger(declaringType);
        if (canLog(method, logger)) {
            String description = descriptionGenerator.generateMethodExit(method, result);
            logger.info("<%s", description);
        }
    }

    private void decreaseIndent() {
        String indentValue = MDC.get(KEY);
        if (indentValue != null && !indentValue.isEmpty()) {
            indentValue = indentValue.substring(INDENT.length());
            MDC.put(KEY, indentValue);
        }
    }

    @AfterReturning("toBeTraced() && ! annotatedMethod()")
    public void reportMethodReturnWithoutResult(JoinPoint joinPoint) {
        decreaseIndent();
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Class<?> declaringType = sig.getDeclaringType();
        Logger logger = LogManager.getFormatterLogger(declaringType);
        Method method = sig.getMethod();
        if (canLog(method, logger)) {
            String description = descriptionGenerator.generateMethodExit(method);
            logger.info("<%s", description);
        }
    }

    @AfterThrowing(value = "toBeTraced()", throwing = "throwable")
    public void reportMethodReturnWithException(JoinPoint joinPoint, Throwable throwable) {
        decreaseIndent();
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Class<?> declaringType = sig.getDeclaringType();
        Logger logger = LogManager.getFormatterLogger(declaringType);
        Method method = sig.getMethod();
        if (canLog(method, logger)) {
            String description = descriptionGenerator.generateMethodThrew(method, throwable);
            logger.error("<%s", description);
        }

    }

    private SortedSet<Integer> determineIndexesOfLoggedParameters(MethodSignature methodSignature) {
        SortedSet<Integer> result = new TreeSet<>();
        Method method = methodSignature.getMethod();
        Annotation[][] annotations = method.getParameterAnnotations();
        int outerCount = annotations.length;
        for (int outer = 0; outer < outerCount; outer++) {
            Annotation[] innerAnnotations = annotations[outer];
            for (Annotation innerAnnotation : innerAnnotations) {
                Class<?> annoClass = innerAnnotation.annotationType();
                if (annoClass.isAssignableFrom(Trace.class)) {
                    result.add(outer);
                    break;
                }
            }
        }
        return result;
    }
}