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

class BreedPostOutputTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = BreedPostOutput.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        BreedPostOutput.Builder builder = BreedPostOutput.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                BreedPostOutput.class);

        BreedPostOutput instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        assertThat(instance.getPopulation()).as("population").isNull();
    }

    @Test
    void constructFilled() {
        Population population = Population.builder().build();
        BreedPostOutput instance = BreedPostOutput.builder()
                .withPopulation(population)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        assertThat(instance.getPopulation()).as("population").isSameAs(population);
    }

    @Test
    void defaultIsValid() {
        BreedPostOutput instance = new BreedPostOutputBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<BreedPostOutput>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}