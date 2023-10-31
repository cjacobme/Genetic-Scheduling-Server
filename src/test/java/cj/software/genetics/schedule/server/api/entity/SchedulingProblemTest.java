package cj.software.genetics.schedule.server.api.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

class SchedulingProblemTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = SchedulingProblem.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        SchedulingProblem.Builder builder = SchedulingProblem.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                SchedulingProblem.class);

        SchedulingProblem instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        assertThat(instance.getPriorities()).isEmpty();
    }

    @Test
    void constructFilled() {
        ProblemPriority prio1 = new ProblemPriorityBuilder().withValue(1).build();
        ProblemPriority prio2 = new ProblemPriorityBuilder().withValue(2).build();
        Collection<ProblemPriority> priorities = List.of(prio2, prio1);
        SchedulingProblem instance = SchedulingProblem.builder()
                .withPriorities(priorities)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        assertThat(instance.getPriorities()).containsExactly(prio1, prio2);
    }

    @Test
    void defaultIsValid() {
        SchedulingProblem instance = new SchedulingProblemBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<SchedulingProblem>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void readFromJson() throws IOException {
        try (InputStream is = Objects.requireNonNull(SchedulingProblemTest.class.getResourceAsStream("SchedulingProblem.json"))) {
            SchedulingProblem expected = SchedulingProblem.builder()
                    .withPriorities(List.of(
                            ProblemPriority.builder()
                                    .withValue(1)
                                    .withSlotCount(150)
                                    .withTasks(List.of(
                                            Task.builder().withIdentifier(1).withDurationValue(10).withDurationUnit(SECONDS).build(),
                                            Task.builder().withIdentifier(2).withDurationValue(20).withDurationUnit(SECONDS).build(),
                                            Task.builder().withIdentifier(3).withDurationValue(1).withDurationUnit(MINUTES).build()
                                    ))
                                    .build(),
                            ProblemPriority.builder()
                                    .withValue(2)
                                    .withSlotCount(80)
                                    .withTasks(List.of(
                                            Task.builder().withIdentifier(101).withDurationValue(15).withDurationUnit(SECONDS).build(),
                                            Task.builder().withIdentifier(102).withDurationValue(1).withDurationUnit(DAYS).build()
                                    ))
                                    .build()
                    )).build();
            ObjectMapper objectMapper = new ObjectMapper();
            SchedulingProblem instance = objectMapper.readValue(is, SchedulingProblem.class);
            assertThat(instance).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}