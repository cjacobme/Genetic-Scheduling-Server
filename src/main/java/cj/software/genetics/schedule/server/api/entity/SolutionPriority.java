package cj.software.genetics.schedule.server.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class SolutionPriority implements Serializable, Comparable<SolutionPriority> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Integer value;

    private final SortedSet<Slot> tasks = new TreeSet<>();

    @JsonIgnore
    private final transient SortedMap<Integer, Task> tasksMap = new TreeMap<>();

    private SolutionPriority() {
    }

    public Integer getValue() {
        return value;
    }

    public SortedMap<Integer, Task> getTasksMap() {
        return Collections.unmodifiableSortedMap(tasksMap);
    }

    public SortedSet<Slot> getSlots() {
        return tasks;
    }

    public void setTaskAt(int index, Task task) {
        Slot slot = Slot.builder().withPosition(index).withTask(task).build();
        this.tasks.add(slot);
        this.tasksMap.put(index, task);
    }

    public Task getTaskAt(int index) {
        Task result = tasksMap.get(index);
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

        public Builder withTasks(SortedMap<Integer, Task> tasks) {
            instance.tasks.clear();
            if (tasks != null) {
                for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                    instance.setTaskAt(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }
    }
}