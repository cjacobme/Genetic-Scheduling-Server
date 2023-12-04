package cj.software.genetics.schedule.server.entity;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class Fitness implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double durationInSeconds;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double fitnessValue;

    private Fitness() {
    }

    public Double getDurationInSeconds() {
        return durationInSeconds;
    }

    public Double getFitnessValue() {
        return fitnessValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected Fitness instance;

        protected Builder() {
            instance = new Fitness();
        }

        public Fitness build() {
            Fitness result = instance;
            instance = null;
            return result;
        }

        public Builder withDurationInSeconds(Double durationInSeconds) {
            instance.durationInSeconds = durationInSeconds;
            return this;
        }

        public Builder withFitnessValue(Double fitnessValue) {
            instance.fitnessValue = fitnessValue;
            return this;
        }
    }
}