package cj.software.genetics.schedule.server.api.entity;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class Slot implements Serializable, Comparable<Slot> {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    private Integer position;

    @NotNull
    @Valid
    private Task task;

    private Slot() {
    }

    public Integer getPosition() {
        return position;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(position);
        int result = builder.build();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject instanceof Slot other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(this.position, other.position);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int compareTo(Slot other) {
        CompareToBuilder builder = new CompareToBuilder()
                .append(this.position, other.position);
        int result = builder.build();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected Slot instance;

        protected Builder() {
            instance = new Slot();
        }

        public Slot build() {
            Slot result = instance;
            instance = null;
            return result;
        }

        public Builder withPosition(Integer position) {
            instance.position = position;
            return this;
        }

        public Builder withTask(Task task) {
            instance.task = task;
            return this;
        }
    }
}