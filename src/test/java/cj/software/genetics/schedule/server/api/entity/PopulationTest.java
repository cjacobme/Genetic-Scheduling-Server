package cj.software.genetics.schedule.server.api.entity;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PopulationTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Population.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Population.Builder builder = Population.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Population.class);

        Population instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        assertThat(instance.getSolutions()).isEmpty();
    }

    @Test
    void constructFilled() {
        List<Solution> solutions = List.of(
                new SolutionBuilder().withGenerationStep(1).withIndexInGeneration(0).build(),
                new SolutionBuilder().withGenerationStep(15).withIndexInGeneration(33).build());
        Population instance = Population.builder()
                .withSolutions(solutions)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        assertThat(instance.getSolutions()).as("solutions").isEqualTo(solutions);
    }

    @Test
    void defaultIsValid() {
        Population instance = new PopulationBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Population>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}