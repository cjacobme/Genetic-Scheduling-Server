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

import static org.assertj.core.api.Assertions.assertThat;

class SchedulingCreatePostInputTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = SchedulingCreatePostInput.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        SchedulingCreatePostInput.Builder builder = SchedulingCreatePostInput.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                SchedulingCreatePostInput.class);

        SchedulingCreatePostInput instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getSchedulingProblem()).as("scheduling problem").isNull();
        softy.assertThat(instance.getSolutionSetup()).as("solution setup").isNull();
        softy.assertThat(instance.getBreedingSetup()).as("breeding setup").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        SchedulingProblem schedulingProblem = SchedulingProblem.builder().build();
        SolutionSetup solutionSetup = SolutionSetup.builder().build();
        BreedingSetup breedingSetup = BreedingSetup.builder().build();
        SchedulingCreatePostInput instance = SchedulingCreatePostInput.builder()
                .withSchedulingProblem(schedulingProblem)
                .withSolutionSetup(solutionSetup)
                .withBreedingSetup(breedingSetup)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getSchedulingProblem()).as("scheduling problem").isSameAs(schedulingProblem);
        softy.assertThat(instance.getSolutionSetup()).as("solution setup").isSameAs(solutionSetup);
        softy.assertThat(instance.getBreedingSetup()).as("breeding setup").isSameAs(breedingSetup);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        SchedulingCreatePostInput instance = new SchedulingCreatePostInputBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<SchedulingCreatePostInput>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}