package cj.software.genetics.schedule.server.api.entity;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ProblemPriority implements Serializable, Comparable<ProblemPriority> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    private Integer value;

    @NotNull
    @Min(1)
    private Integer slotCount;

    @NotEmpty
    private final Collection<@Valid Task> tasks = new ArrayList<>();

    private ProblemPriority() {
    }

    public Integer getValue() {
        return value;
    }

    public Integer getSlotCount() {
        return slotCount;
    }

    public Collection<Task> getTasks() {
        return Collections.unmodifiableCollection(tasks);
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("value", value)
                .append("slot count", slotCount)
                .append("task count", tasks.size());
        String result = builder.build();
        return result;
    }

    @Override
    public boolean equals (Object otherObject) {
        boolean result;
        if (otherObject instanceof ProblemPriority other) {
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
    public int compareTo(ProblemPriority other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.value, other.value);
        int result = builder.build();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected ProblemPriority instance;

        protected Builder() {
            instance = new ProblemPriority();
        }

        public ProblemPriority build() {
            ProblemPriority result = instance;
            instance = null;
            return result;
        }

        public Builder withValue(Integer value) {
            instance.value = value;
            return this;
        }

        public Builder withSlotCount(Integer slotCount) {
            instance.slotCount = slotCount;
            return this;
        }

        public Builder withTasks(Collection<Task> tasks) {
            instance.tasks.clear();
            if (tasks != null) {
                instance.tasks.addAll(tasks);
            }
            return this;
        }
    }
}