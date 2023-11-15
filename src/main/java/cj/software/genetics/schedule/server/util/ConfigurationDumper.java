package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.Constants;
import cj.software.genetics.schedule.server.entity.configuration.ConfigurationHolder;
import cj.software.genetics.schedule.server.entity.configuration.GeneralConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationDumper implements InitializingBean {

    private static final String STRING_FORMAT = "%50.50s = %s";

    private static final String INT_FORMAT = "%50.50s = %d";

    @SuppressWarnings("java:S3749") // for Loggers OK
    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private ConfigurationHolder configurationHolder;

    @Override
    public void afterPropertiesSet() {
        try (MDC.MDCCloseable ignored = MDC.putCloseable(Constants.CORRELATION_ID_KEY, "config")) {
            logger.info("##############################################################################");
            logger.info("#####     List of all properties                                         #####");
            logger.info("#####                                                                    #####");
            logger.info("+++       General configuration                                            +++");
            log(configurationHolder.getGeneral());
            logger.info(INT_FORMAT, "The answer to all", 42);
            logger.info("##############################################################################");
        }
    }

    private void log(GeneralConfiguration configuration) {
        logger.info(STRING_FORMAT, "backend version", configuration.getBackendVersion());
        logger.info(STRING_FORMAT, "backend built", configuration.getBackendBuilt());
    }
}
