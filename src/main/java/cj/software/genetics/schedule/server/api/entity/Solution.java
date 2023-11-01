package cj.software.genetics.schedule.server.api.entity;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solution implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    private Integer generationStep;

    @NotNull
    @Min(0)
    private Integer indexInPopulation;

    @NotNull
    private Double fitnessValue;

    @NotEmpty
    private final List<@NotNull @Valid Worker> workers = new ArrayList<>();

    private Solution() {
    }

    public Integer getGenerationStep() {
        return generationStep;
    }

    public Integer getIndexInPopulation() {
        return indexInPopulation;
    }

    public Double getFitnessValue() {
        return fitnessValue;
    }

    public List<Worker> getWorkers() {
        return Collections.unmodifiableList(workers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected Solution instance;

        protected Builder() {
            instance = new Solution();
        }

        public Solution build() {
            Solution result = instance;
            instance = null;
            return result;
        }

        public Builder withGenerationStep(Integer generationStep) {
            instance.generationStep = generationStep;
            return this;
        }

        public Builder withIndexInGeneration(Integer indexInGeneration) {
            instance.indexInPopulation = indexInGeneration;
            return this;
        }

        public Builder withFitnessValue(double fitnessValue) {
            instance.fitnessValue = fitnessValue;
            return this;
        }

        public Builder withWorkers(List<Worker> workers) {
            instance.workers.clear();
            if (workers != null) {
                instance.workers.addAll(workers);
            }
            return this;
        }
    }
}