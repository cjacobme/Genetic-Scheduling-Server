package cj.software.genetics.schedule.server.api.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class BreedingSetup implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Integer loopCount;

    private BreedingSetup() {
    }

    public Integer getLoopCount() {
        return loopCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected BreedingSetup instance;

        protected Builder() {
            instance = new BreedingSetup();
        }

        public BreedingSetup build() {
            BreedingSetup result = instance;
            instance = null;
            return result;
        }

        public Builder withLoopCount(Integer loopCount) {
            instance.loopCount = loopCount;
            return this;
        }
    }
}