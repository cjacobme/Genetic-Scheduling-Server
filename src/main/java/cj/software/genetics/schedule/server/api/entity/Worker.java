package cj.software.genetics.schedule.server.api.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class Worker implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private final SortedSet<@Valid SolutionPriority> priorities = new TreeSet<>();

    private Worker() {
    }

    public SortedSet<SolutionPriority> getPriorities() {
        return Collections.unmodifiableSortedSet(priorities);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected Worker instance;

        protected Builder() {
            instance = new Worker();
        }

        public Worker build() {
            Worker result = instance;
            instance = null;
            return result;
        }

        public Builder withPriorities(Collection<SolutionPriority> priorities) {
            instance.priorities.clear();
            if (priorities != null) {
                instance.priorities.addAll(priorities);
            }
            return this;
        }
    }
}