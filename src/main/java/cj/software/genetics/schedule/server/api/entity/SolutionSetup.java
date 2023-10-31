package cj.software.genetics.schedule.server.api.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class SolutionSetup implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Integer solutionCount;

    @NotNull
    @Min(1)
    private Integer workersPerSolutionCount;

    @NotNull
    @Min(0)
    private Integer elitismCount;

    @NotNull
    @Min(0)
    private Integer tournamentSize;

    private SolutionSetup() {
    }

    public Integer getSolutionCount() {
        return solutionCount;
    }

    public Integer getWorkersPerSolutionCount() {
        return workersPerSolutionCount;
    }

    public Integer getElitismCount() {
        return elitismCount;
    }

    public Integer getTournamentSize() {
        return tournamentSize;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected SolutionSetup instance;

        protected Builder() {
            instance = new SolutionSetup();
        }

        public SolutionSetup build() {
            SolutionSetup result = instance;
            instance = null;
            return result;
        }

        public Builder withSolutionCount(Integer solutionCount) {
            instance.solutionCount = solutionCount;
            return this;
        }

        public Builder withWorkersPerSolutionCount(Integer workersPerSolutionCount) {
            instance.workersPerSolutionCount = workersPerSolutionCount;
            return this;
        }

        public Builder withElitismCount(Integer elitismCount) {
            instance.elitismCount = elitismCount;
            return this;
        }

        public Builder withTournamentSize(Integer tournamentSize) {
            instance.tournamentSize = tournamentSize;
            return this;
        }
    }
}