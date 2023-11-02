package cj.software.genetics.schedule.server.api.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class SchedulingCreatePostInput implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Valid
    private SchedulingProblem schedulingProblem;

    @NotNull
    @Valid
    private SolutionSetup solutionSetup;

    private SchedulingCreatePostInput() {
    }

    public SchedulingProblem getSchedulingProblem() {
        return schedulingProblem;
    }

    public SolutionSetup getSolutionSetup() {
        return solutionSetup;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected SchedulingCreatePostInput instance;

        protected Builder() {
            instance = new SchedulingCreatePostInput();
        }

        public SchedulingCreatePostInput build() {
            SchedulingCreatePostInput result = instance;
            instance = null;
            return result;
        }

        public Builder withSchedulingProblem(SchedulingProblem schedulingProblem) {
            instance.schedulingProblem = schedulingProblem;
            return this;
        }

        public Builder withSolutionSetup(SolutionSetup solutionSetup) {
            instance.solutionSetup = solutionSetup;
            return this;
        }
    }
}