package cj.software.genetics.schedule.server.api.entity;

import cj.software.genetics.schedule.server.api.converter.DurationDeserializer;
import cj.software.genetics.schedule.server.api.converter.DurationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonDeserialize(using = DurationDeserializer.class)
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    @NotNull
    private Integer identifier;

    private Task() {
    }

    public Duration getDuration() {
        return duration;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", this.identifier)
                .append("duration", this.duration);
        String result = builder.build();
        return result;
    }

    @Override
    public boolean equals(Object otherObject) {
        boolean result;
        if (otherObject instanceof Task other) {
            EqualsBuilder builder = new EqualsBuilder()
                    .append(this.identifier, other.identifier);
            result = builder.build();
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
                .append(this.identifier);
        int result = builder.build();
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

        public Builder withIdentifier(Integer identifier) {
            instance.identifier = identifier;
            return this;
        }

        public Builder withDuration(Duration duration) {
            instance.duration = duration;
            return this;
        }
    }
}