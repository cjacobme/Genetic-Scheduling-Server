package cj.software.genetics.schedule.server.api.entity;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulingCreatePostOutputTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = SchedulingCreatePostOutput.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        SchedulingCreatePostOutput.Builder builder = SchedulingCreatePostOutput.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                SchedulingCreatePostOutput.class);

        SchedulingCreatePostOutput instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        assertThat(instance.getPopulation()).as("population").isNull();
    }

    @Test
    void constructFilled() {
        Population population = Population.builder().build();
        SchedulingCreatePostOutput instance = SchedulingCreatePostOutput.builder()
                .withPopulation(population)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        assertThat(instance.getPopulation()).as("population").isSameAs(population);
    }

    @Test
    void defaultIsValid() {
        SchedulingCreatePostOutput instance = new SchedulingCreatePostOutputBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<SchedulingCreatePostOutput>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}