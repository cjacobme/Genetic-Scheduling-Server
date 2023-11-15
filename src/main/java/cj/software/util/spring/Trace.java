package cj.software.util.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * marks whether a value participating in a method invocation should be traced by the {@link TraceAspect}. If we have
 * a Java class {@code MyClass} with a method like that:
 * <pre>
 * {@code
 * public @Trace String doSomething (
 *     @Trace String param1,
 *     String param2) {
 *         // ...
 *     }
 * }
 * </pre>
 * <p>
 * and some other Java method would invoke
 * <pre>
 *     {@code
 *     myClass.doSomething("value1", "value2");
 *     }
 * </pre>
 * <p>
 * and the MyClass implementation would return the String {@code "VALUE1VALUE2"}
 * <p>
 * then the {@link TraceAspect} would produce an entries like that:
 * <pre>
 *     {@code
 *     >MyClass.doSomething(param1=value1)
 *     <MyClass.doSomething(..) returns VALUE1VALUE2
 *     }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
public @interface Trace {
}
