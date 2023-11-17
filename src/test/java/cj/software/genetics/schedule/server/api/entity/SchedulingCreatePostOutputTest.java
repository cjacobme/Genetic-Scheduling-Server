package cj.software.genetics.schedule.server.api.entity;

import cj.software.genetics.schedule.server.entity.ValidatingTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulingCreatePostOutputTest extends ValidatingTest {

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
        validate(instance);
    }

    @Test
    void loadFromJson() throws IOException {
        try (InputStream is = Objects.requireNonNull(SchedulingCreatePostOutputTest.class.getResourceAsStream("SchedulingCreatePostOutput.json"))) {
            ObjectMapper objectMapper = new ObjectMapper();
            SchedulingCreatePostOutput loaded = objectMapper.readValue(is, SchedulingCreatePostOutput.class);
            assertThat(loaded).isNotNull();
            validate(loaded);
            Population population = loaded.getPopulation();
            assertThat(population).isNotNull();
            List<Solution> solutions = population.getSolutions();
            assertThat(solutions).hasSize(1);
            Solution solution = solutions.get(0);
            List<Worker> workers = solution.getWorkers();
            SoftAssertions softy = new SoftAssertions();
            softy.assertThat(solution.getIndexInPopulation()).as("index in population").isEqualTo(1);
            softy.assertThat(solution.getGenerationStep()).as("generation step").isEqualTo(0);
            softy.assertThat(solution.getFitnessValue()).as("fitness value").isEqualTo(3.14);
            softy.assertThat(workers).as("workers").hasSize(1);
            softy.assertAll();
            Worker worker = workers.get(0);
            SortedSet<SolutionPriority> priorities = worker.getPriorities();
            assertThat(priorities).as("priorities").hasSize(1);
            SolutionPriority priority = priorities.first();
            SortedMap<Integer, Task> tasks = priority.getTasks();
            SortedMap<Integer, Task> expected = new TreeMap<>();
            expected.put(2, Task.builder()
                    .withIdentifier(3)
                    .withDuration(Duration.ofMinutes(1))
                    .build());
            expected.put(25, Task.builder()
                    .withIdentifier(2)
                    .withDuration(Duration.ofSeconds(20))
                    .build());
            expected.put(4711, Task.builder()
                    .withIdentifier(123)
                    .withDuration(Duration.ofSeconds(10))
                    .build());
            assertThat(tasks).as("tasks").usingRecursiveComparison().isEqualTo(expected);
        }
    }
}