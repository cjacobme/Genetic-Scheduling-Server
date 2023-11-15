package cj.software.genetics.schedule.server.api.util;

import cj.software.genetics.schedule.server.Constants;
import cj.software.genetics.schedule.server.entity.configuration.ConfigurationHolder;
import cj.software.genetics.schedule.server.entity.configuration.GeneralConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
@Component
public class HttpHeadersManager implements Filter {
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    @Autowired
    private ConfigurationHolder configurationHolder;

    private final Logger logger = LogManager.getFormatterLogger();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String oldCorrelationId = MDC.get(Constants.CORRELATION_ID_KEY);
        try {
            String correlationId = constructCorrelationId(httpServletRequest);
            MDC.put(Constants.CORRELATION_ID_KEY, correlationId);
            String requestDescription = describe(httpServletRequest);
            logger.info("processing %s...", requestDescription);
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            GeneralConfiguration generalConfiguration = configurationHolder.getGeneral();
            httpServletResponse.addHeader("cj.software.backend-version", generalConfiguration.getBackendVersion());
            httpServletResponse.addHeader("cj.software.backend-built", generalConfiguration.getBackendBuilt());
            httpServletResponse.addHeader(Constants.CORRELATION_ID_KEY, correlationId);

            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            MDC.put(Constants.CORRELATION_ID_KEY, oldCorrelationId);
        }
    }

    private String constructCorrelationId(HttpServletRequest request) {
        String existing = request.getHeader(Constants.CORRELATION_ID_KEY);
        String result;
        if (existing != null && !existing.isBlank()) {
            result = existing;
        } else {
            result = UUID.randomUUID().toString();
        }
        return result;
    }

    private String describe(HttpServletRequest request) {
        String remoteAddress = determineCaller(request);
        StringBuilder sb = new StringBuilder()
                .append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI())
                .append(" from ")
                .append(remoteAddress);
        String result = sb.toString();
        return result;
    }

    private String determineCaller(HttpServletRequest request) {
        String result = null;
        for (String header : HEADERS_TO_TRY) {
            result = request.getHeader(header);
            if (result != null && !result.isBlank()) {
                break;
            }
        }
        if (result == null) {
            result = request.getRemoteAddr();
        }
        return result;
    }
}
