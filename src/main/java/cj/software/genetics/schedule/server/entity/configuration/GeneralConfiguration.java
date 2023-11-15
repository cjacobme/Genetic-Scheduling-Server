package cj.software.genetics.schedule.server.entity.configuration;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

public class GeneralConfiguration implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String backendVersion;

    @NotBlank
    private String backendBuilt;

    private GeneralConfiguration() {
    }

    public String getBackendVersion() {
        return backendVersion;
    }

    public void setBackendVersion(String backendVersion) {
        this.backendVersion = backendVersion;
    }

    public String getBackendBuilt() {
        return backendBuilt;
    }

    public void setBackendBuilt(String backendBuilt) {
        this.backendBuilt = backendBuilt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected GeneralConfiguration instance;

        protected Builder() {
            instance = new GeneralConfiguration();
        }

        public GeneralConfiguration build() {
            GeneralConfiguration result = instance;
            instance = null;
            return result;
        }

        public Builder withBackendVersion(String backendVersion) {
            instance.setBackendVersion(backendVersion);
            return this;
        }

        public Builder withBackendBuilt(String backendBuilt) {
            instance.setBackendBuilt(backendBuilt);
            return this;
        }
    }
}