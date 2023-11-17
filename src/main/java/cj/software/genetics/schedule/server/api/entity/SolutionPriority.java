package cj.software.genetics.schedule.server.api.entity;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SolutionPriority implements Serializable, Comparable<SolutionPriority> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Integer value;

    private final SortedMap<Integer, Task> tasks = new TreeMap<>();

    private SolutionPriority() {
    }

    public Integer getValue() {
        return value;
    }

    public SortedMap<Integer, Task> getTasks() {
        return Collections.unmodifiableSortedMap(tasks);
    }

    public void setTaskAt(int index, Task task) {
        this.tasks.put(index, task);
    }

    public Task getTaskAt(int index) {
        Task result = tasks.get(index);
        return result;
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

    @Override
    public int compareTo(SolutionPriority other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.value, other.value);
        int result = builder.build();
        return result;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("value", value);
        String result = builder.build();
        return result;
    }

    public static Builder builder() {
        return new Builder();
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

        public Builder withTasks(Map<Integer, Task> tasks) {
            instance.tasks.clear();
            if (tasks != null) {
                instance.tasks.putAll(tasks);
            }
            return this;
        }
    }
}