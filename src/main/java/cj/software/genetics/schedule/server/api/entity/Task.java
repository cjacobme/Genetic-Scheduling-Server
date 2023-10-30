package cj.software.genetics.schedule.server.api.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Integer durationValue;

    @NotNull
    private TimeUnit durationUnit;

    private Task() {
    }

    public Integer getDurationValue() {
        return durationValue;
    }

    public TimeUnit getDurationUnit() {
        return durationUnit;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("duration", this.durationValue)
                .append("unit", this.durationUnit);
        String result = builder.build();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        protected Task instance;

        protected Builder() {
            instance = new Task();
        }

        public Task build() {
            Task result = instance;
            instance = null;
            return result;
        }

        public Builder withDurationValue(Integer durationValue) {
            instance.durationValue = durationValue;
            return this;
        }

        public Builder withDurationUnit(TimeUnit durationUnit) {
            instance.durationUnit = durationUnit;
            return this;
        }
    }
}