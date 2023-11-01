package cj.software.genetics.schedule.server.api.entity;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Task.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Task.Builder builder = Task.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Task.class);

        Task instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getDurationValue()).as("duration value").isNull();
        softy.assertThat(instance.getDurationUnit()).as("duration unit").isNull();
        softy.assertThat(instance.getIdentifier()).as("identifier").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer durationValue = -10;
        TimeUnit durationUnit = TimeUnit.DAYS;
        Integer identifier = 1;
        Task instance = Task.builder()
                .withIdentifier(identifier)
                .withDurationValue(durationValue)
                .withDurationUnit(durationUnit)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getIdentifier()).as("identifier").isEqualTo(identifier);
        softy.assertThat(instance.getDurationValue()).as("duration value").isEqualTo(durationValue);
        softy.assertThat(instance.getDurationUnit()).as("duration unit").isEqualTo(durationUnit);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Task instance = new TaskBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Task>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void stringPresentation() {
        Task instance = new TaskBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("Task[id=4243,duration=10,unit=SECONDS]");
    }

    @Test
    void equalObjects() {
        Task instance1 = new TaskBuilder().build();
        Task instance2 = new TaskBuilder().withDurationValue(223).withDurationUnit(TimeUnit.HOURS).build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void equalHashes() {
        Task instance1 = new TaskBuilder().build();
        Task instance2 = new TaskBuilder().withDurationValue(223).withDurationUnit(TimeUnit.HOURS).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalId() {
        Task instance1 = new TaskBuilder().build();
        Task instance2 = new TaskBuilder().withIdentifier(54321).build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalIdHashes() {
        Task instance1 = new TaskBuilder().build();
        Task instance2 = new TaskBuilder().withIdentifier(54321).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void unequalOtherObject() {
        Task instance1 = new TaskBuilder().build();
        Object instance2 = "other object";
        assertThat(instance1).isNotEqualTo(instance2);
    }
}