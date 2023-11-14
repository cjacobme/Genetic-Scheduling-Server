package cj.software.genetics.schedule.server.api.entity;

import cj.software.genetics.schedule.server.util.json.PostLoad;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Worker implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty
    private final SortedSet<@Valid SolutionPriority> priorities = new TreeSet<>();

    final transient Map<Integer, SolutionPriority> asMap = new HashMap<>();

    private Worker() {
    }

    @PostLoad
    public void postLoad() {
        for (SolutionPriority priority : priorities) {
            asMap.put(priority.getValue(), priority);
        }
    }

    public SortedSet<SolutionPriority> getPriorities() {
        return Collections.unmodifiableSortedSet(priorities);
    }

    public SolutionPriority getPriority(int value) {
        SolutionPriority result = asMap.get(value);
        return result;
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
                for (SolutionPriority priority : priorities) {
                    instance.asMap.put(priority.getValue(), priority);
                }
            }
            return this;
        }
    }
}