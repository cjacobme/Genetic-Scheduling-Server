package cj.software.genetics.schedule.server.api.entity;

import cj.software.util.spring.BeanProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulingCreatePostInputTest extends ValidatingTest {

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
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        SchedulingProblem schedulingProblem = SchedulingProblem.builder().build();
        SolutionSetup solutionSetup = SolutionSetup.builder().build();
        SchedulingCreatePostInput instance = SchedulingCreatePostInput.builder()
                .withSchedulingProblem(schedulingProblem)
                .withSolutionSetup(solutionSetup)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getSchedulingProblem()).as("scheduling problem").isSameAs(schedulingProblem);
        softy.assertThat(instance.getSolutionSetup()).as("solution setup").isSameAs(solutionSetup);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        SchedulingCreatePostInput instance = new SchedulingCreatePostInputBuilder().build();
        super.validate(instance);
    }

    @Test
    void loadFromJson() throws IOException {
        try (InputStream is = Objects.requireNonNull(
                SchedulingCreatePostInputTest.class.getResourceAsStream("SchedulingCreatePostInput.json"))) {
            ObjectMapper objectMapper = new BeanProducer().objectMapper();
            SchedulingCreatePostInput instance = objectMapper.readValue(is, SchedulingCreatePostInput.class);
            assertThat(instance).as("loaded instance").isNotNull();
            validate(instance);
        }
    }
}