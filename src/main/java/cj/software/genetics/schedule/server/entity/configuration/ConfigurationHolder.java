package cj.software.genetics.schedule.server.entity.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "cj.software.genetics.server")
@Validated
@EnableAspectJAutoProxy
public class ConfigurationHolder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Valid
    private GeneralConfiguration general;

    ConfigurationHolder() {
    }

    public GeneralConfiguration getGeneral() {
        return general;
    }

    public void setGeneral(GeneralConfiguration general) {
        this.general = general;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected ConfigurationHolder instance;

        protected Builder() {
            instance = new ConfigurationHolder();
        }

        public ConfigurationHolder build() {
            ConfigurationHolder result = instance;
            instance = null;
            return result;
        }

        public Builder withGeneral(GeneralConfiguration general) {
            instance.setGeneral(general);
            return this;
        }
    }
}