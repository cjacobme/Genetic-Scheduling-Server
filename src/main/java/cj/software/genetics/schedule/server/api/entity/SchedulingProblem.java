package cj.software.genetics.schedule.server.api.entity;

import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
public class SchedulingProblem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private final SortedSet<ProblemPriority> priorities = new TreeSet<>();

    private SchedulingProblem() {
    }

    public SortedSet<ProblemPriority> getPriorities() {
        return Collections.unmodifiableSortedSet(priorities);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected SchedulingProblem instance;

        protected Builder() {
            instance = new SchedulingProblem();
        }

        public SchedulingProblem build() {
            SchedulingProblem result = instance;
            instance = null;
            return result;
        }

        public Builder withPriorities(Collection<ProblemPriority> priorities) {
            instance.priorities.clear();
            if (priorities != null) {
                instance.priorities.addAll(priorities);
            }
            return this;
        }
    }
}