package cj.software.util.spring;

import org.apache.logging.log4j.spi.StandardLevel;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.SortedSet;

@Service
@TraceAtLogLevel(level = StandardLevel.TRACE)
public class MethodDescriptionGenerator {
    /**
     * generates a description of a method entry. This description contains the name of the class and the name of the
     * method. If the {@link List} parameterIndexesToBeReported is not empty, each entry of this {@link List} is used
     * to obtain the indexed parameter name and value and add that to the description.<br/>
     * Example:
     * <ul>
     *     <li>class name = {@code MyClass}</li>
     *     <li>method name = {@code myMethod}</li>
     *     <li>parameter names = {@code {"one", "two", "three"}}</li>
     *     <li>parameter values = {@code {1, "Hello", false}}</li>
     *     <li>indexes of parameters to be reported = {@code {1}}</li>
     * </ul>
     * In that case, the result would be <pre>
     *     {@code MyClass.myMethod(two=Hello)}
     * </pre>
     *
     * @param method                       the method to be reported
     * @param parameterNames               names of the parameters
     * @param parameterValues              values of the parameter
     * @param parameterIndexesToBeReported indexes for parameter for which the (name, value) pair has to be added to
     *                                     the result
     * @return description of the method entry
     */
    public String generateMethodEntry(
            Method method,
            String[] parameterNames,
            Object[] parameterValues,
            @SuppressWarnings("java:S3242") // I really want to have it as a SortedSet
            SortedSet<Integer> parameterIndexesToBeReported) {
        StringBuilder sb = methodBegin(method);
        if (!parameterIndexesToBeReported.isEmpty()) {
            for (int index : parameterIndexesToBeReported) {
                String parameterName = parameterNames[index];
                Object parameterValue = parameterValues[index];
                String formatted = String.format("%s=%s,", parameterName, parameterValue);
                sb.append(formatted);
            }
            int length = sb.length();
            sb.deleteCharAt(length - 1);
        }
        sb.append(")");
        String result = sb.toString();
        return result;
    }

    private StringBuilder methodBegin(Method method) {
        String methodName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        StringBuilder result = new StringBuilder(className).append(".").append(methodName);
        result.append("(");
        return result;
    }

    /**
     * generates a description of a method return without the returned value
     *
     * @param method the method to be described
     * @return the generated description
     */
    public String generateMethodExit(Method method) {
        StringBuilder sb = methodBegin(method);
        sb.append(")");
        return sb.toString();
    }

    /**
     * generate a description of a method together with the returned value
     *
     * @param method      the method to be described
     * @param returnValue the value returned by the method
     * @return the generated description
     */
    public String generateMethodExit(Method method, Object returnValue) {
        StringBuilder sb = methodBegin(method);
        sb.append(") returns ");
        String formatted = String.format("%s", returnValue);
        sb.append(formatted);
        return sb.toString();
    }

    public String generateMethodThrew(Method method, Throwable throwable) {
        StringBuilder sb = methodBegin(method);
        sb.append(")").append(" threw ").append(throwable.getClass().getSimpleName());
        return sb.toString();
    }
}
