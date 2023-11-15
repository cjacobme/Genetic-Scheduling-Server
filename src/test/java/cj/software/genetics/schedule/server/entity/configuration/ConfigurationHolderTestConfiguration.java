package cj.software.genetics.schedule.server.entity.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ConfigurationHolderTestConfiguration {
    @Bean("configurationHolder")
    public ConfigurationHolder configurationHolder() {
        ConfigurationHolder result = new ConfigurationHolderBuilder().build();
        return result;
    }
}
