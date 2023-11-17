package cj.software.genetics.schedule.server.api.entity;

import cj.software.genetics.schedule.server.entity.ValidatingTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class SolutionSetupTest extends ValidatingTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = SolutionSetup.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        SolutionSetup.Builder builder = SolutionSetup.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                SolutionSetup.class);

        SolutionSetup instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getSolutionCount()).as("solution count").isNull();
        softy.assertThat(instance.getWorkersPerSolutionCount()).as("workers per solution count").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer solutionCount = -1;
        Integer workersPerSolutionCount = -2;
        SolutionSetup instance = SolutionSetup.builder()
                .withSolutionCount(solutionCount)
                .withWorkersPerSolutionCount(workersPerSolutionCount)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getSolutionCount()).as("solution count").isEqualTo(solutionCount);
        softy.assertThat(instance.getWorkersPerSolutionCount()).as("workers per solution count").isEqualTo(workersPerSolutionCount);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        SolutionSetup instance = new SolutionSetupBuilder().build();
        validate(instance);
    }

    @Test
    void loadFromJson() throws IOException {
        try (InputStream is = Objects.requireNonNull(SolutionSetupTest.class.getResourceAsStream("SolutionSetup.json"))) {
            ObjectMapper objectMapper = new ObjectMapper();
            SolutionSetup loaded = objectMapper.readValue(is, SolutionSetup.class);
            validate(loaded);
            SolutionSetup expected = SolutionSetup.builder()
                    .withSolutionCount(101)
                    .withWorkersPerSolutionCount(4)
                    .build();
            assertThat(loaded).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}