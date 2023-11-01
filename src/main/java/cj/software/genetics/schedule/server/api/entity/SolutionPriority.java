package cj.software.genetics.schedule.server.api.entity;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionPriority implements Serializable, Comparable<SolutionPriority> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Integer value;

    @NotEmpty
    private final List<@Valid Task> tasks = new ArrayList<>();

    private SolutionPriority() {
    }

    public Integer getValue() {
        return value;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    public void setTaskAt(int index, Task task) {
        this.tasks.set(index, task);
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject instanceof SolutionPriority other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(this.value, other.value);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(value);
        int result = builder.build();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int compareTo(SolutionPriority other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.value, other.value);
        int result = builder.build();
        return result;
    }

    public static class Builder {
        protected SolutionPriority instance;

        protected Builder() {
            instance = new SolutionPriority();
        }

        public SolutionPriority build() {
            SolutionPriority result = instance;
            instance = null;
            return result;
        }

        public Builder withValue(Integer value) {
            instance.value = value;
            return this;
        }

        public Builder withTasks(List<Task> tasks) {
            instance.tasks.clear();
            if (tasks != null) {
                instance.tasks.addAll(tasks);
            }
            return this;
        }
    }
}