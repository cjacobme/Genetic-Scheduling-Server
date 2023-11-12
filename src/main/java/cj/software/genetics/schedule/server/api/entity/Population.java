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

public class Population implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    private Integer generationStep;

    @NotEmpty
    private final List<@NotNull @Valid Solution> solutions = new ArrayList<>();

    private Population() {
    }

    public Integer getGenerationStep() {
        return generationStep;
    }

    public List<Solution> getSolutions() {
        return Collections.unmodifiableList(solutions);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void postLoad() {
        for (Solution solution : solutions) {
            solution.postLoad();
        }
    }

    public static class Builder {
        protected Population instance;

        protected Builder() {
            instance = new Population();
        }

        public Population build() {
            Population result = instance;
            instance = null;
            return result;
        }

        public Builder withSolutions(List<Solution> solutions) {
            instance.solutions.clear();
            if (solutions != null) {
                instance.solutions.addAll(solutions);
            }
            return this;
        }

        public Builder withGenerationStep(Integer generationStep) {
            instance.generationStep = generationStep;
            return this;
        }
    }
}