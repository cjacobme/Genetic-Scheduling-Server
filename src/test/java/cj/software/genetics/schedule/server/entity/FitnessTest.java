package cj.software.genetics.schedule.server.entity;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FitnessTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Fitness.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Fitness.Builder builder = Fitness.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Fitness.class);

        Fitness instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getDurationInSeconds()).as("duration in seconds").isNull();
        softy.assertThat(instance.getFitnessValue()).as("fitness value").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Double durationInSeconds = 0.1;
        Double fitnessValue = 0.2;
        Fitness instance = Fitness.builder()
                .withDurationInSeconds(durationInSeconds)
                .withFitnessValue(fitnessValue)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getDurationInSeconds()).as("duration in seconds").isEqualTo(durationInSeconds);
        softy.assertThat(instance.getFitnessValue()).as("fitness value").isEqualTo(fitnessValue);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Fitness instance = new FitnessBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Fitness>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}