package cj.software.genetics.schedule.server.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

public class Coordinate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer workerIndex;

    @NotNull
    private Integer slotIndex;

    private Coordinate() {
    }

    public Integer getWorkerIndex() {
        return workerIndex;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(workerIndex)
                .append(slotIndex);
        String result = builder.build();
        return result;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(workerIndex)
                .append(slotIndex);
        int result = builder.build();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject instanceof Coordinate other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(this.workerIndex, other.workerIndex)
                    .append(this.slotIndex, other.slotIndex);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected Coordinate instance;

        protected Builder() {
            instance = new Coordinate();
        }

        public Coordinate build() {
            Coordinate result = instance;
            instance = null;
            return result;
        }

        public Builder withWorkerIndex(Integer workerIndex) {
            instance.workerIndex = workerIndex;
            return this;
        }

        public Builder withSlotIndex(Integer slotIndex) {
            instance.slotIndex = slotIndex;
            return this;
        }
    }
}