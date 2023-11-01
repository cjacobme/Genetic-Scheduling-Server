package cj.software.genetics.schedule.server.api.entity;

import org.assertj.core.api.SoftAssertions;
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

class SolutionTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Solution.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Solution.Builder builder = Solution.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Solution.class);

        Solution instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getGenerationStep()).as("generation step").isNull();
        softy.assertThat(instance.getIndexInPopulation()).as("index in population").isNull();
        softy.assertThat(instance.getFitnessValue()).as("fitness value").isNull();
        softy.assertThat(instance.getWorkers()).as("workers").isEmpty();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer generationStep = -1;
        Integer indexInGeneration = -2;
        double fitnessValue = -3.14;
        List<Worker> workers = List.of(new WorkerBuilder().build(), new WorkerBuilder().build());
        Solution instance = Solution.builder()
                .withGenerationStep(generationStep)
                .withIndexInGeneration(indexInGeneration)
                .withFitnessValue(fitnessValue)
                .withWorkers(workers)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getGenerationStep()).as("generation step").isEqualTo(generationStep);
        softy.assertThat(instance.getIndexInPopulation()).as("index in generation").isEqualTo(indexInGeneration);
        softy.assertThat(instance.getFitnessValue()).as("fitness value").isEqualTo(fitnessValue);
        softy.assertThat(instance.getWorkers()).as("workers").isEqualTo(workers);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Solution instance = new SolutionBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Solution>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}