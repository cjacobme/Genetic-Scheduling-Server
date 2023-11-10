package cj.software.genetics.schedule.server.api.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

class BreedPostInputTest {

    private static ValidatorFactory factory;

    private static Validator validator;

    @BeforeAll
    static void createValidation() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void shutdownValidation() {
        factory.close();
    }

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = BreedPostInput.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        BreedPostInput.Builder builder = BreedPostInput.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                BreedPostInput.class);

        BreedPostInput instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getNumSteps()).as("number of steps").isNull();
        softy.assertThat(instance.getElitismCount()).as("elitism count").isNull();
        softy.assertThat(instance.getTournamentSize()).as("tournament size").isNull();
        softy.assertThat(instance.getMutationRate()).as("mutation rate").isNull();
        softy.assertThat(instance.getPopulation()).as("population").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer numSteps = -1;
        Integer elitismCount = -3;
        Integer tournamentSize = -4;
        Double mutationRate = -1.2;
        Population population = Population.builder().build();

        BreedPostInput instance = BreedPostInput.builder()
                .withNumSteps(numSteps)
                .withElitismCount(elitismCount)
                .withTournamentSize(tournamentSize)
                .withMutationRate(mutationRate)
                .withPopulation(population)
                .build();

        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getNumSteps()).as("num steps").isEqualTo(numSteps);
        softy.assertThat(instance.getElitismCount()).as("elitism count").isEqualTo(elitismCount);
        softy.assertThat(instance.getTournamentSize()).as("tournament size").isEqualTo(tournamentSize);
        softy.assertThat(instance.getMutationRate()).as("mutation rate").isEqualTo(mutationRate);
        softy.assertThat(instance.getPopulation()).as("population").isSameAs(population);
        softy.assertAll();
    }

    private void validate(BreedPostInput instance) {
        Set<ConstraintViolation<BreedPostInput>> violations = validator.validate(instance);
        assertThat(violations).as("constraint violations").isEmpty();
    }

    @Test
    void defaultIsValid() {
        BreedPostInput instance = new BreedPostInputBuilder().build();
        validate(instance);
    }

    @Test
    void loadFromJson() throws IOException {
        try (InputStream is = Objects.requireNonNull(BreedPostInputTest.class.getResourceAsStream("BreedPostInput.json"))) {
            ObjectMapper objectMapper = new ObjectMapper();
            BreedPostInput instance = objectMapper.readValue(is, BreedPostInput.class);
            validate(instance);
            BreedPostInput expected = createExpectedFromJson();
            assertThat(instance)
                    .as("read from json")
                    .usingRecursiveComparison()
                    .ignoringFields("population.solutions[0].workers[0].asMap")
                    .isEqualTo(expected);
        }
    }

    private BreedPostInput createExpectedFromJson() {
        SortedMap<Integer, Task> tasks = new TreeMap<>();
        tasks.put(2, Task.builder().withDuration(Duration.ofMinutes(2)).withIdentifier(32).build());
        tasks.put(25, Task.builder().withDuration(Duration.ofSeconds(30)).withIdentifier(4).build());
        Collection<SolutionPriority> priorities = List.of(
                SolutionPriority.builder()
                        .withValue(1)
                        .withTasks(tasks)
                        .build());
        Worker worker = Worker.builder()
                .withPriorities(priorities)
                .build();
        worker.asMap.clear();
        List<Worker> workers = List.of(worker);
        List<Solution> solutions = List.of(
                Solution.builder()
                        .withGenerationStep(2)
                        .withIndexInPopulation(3)
                        .withFitnessValue(2.95)
                        .withWorkers(workers)
                        .build());
        Population population = Population.builder()
                .withGenerationStep(3)
                .withSolutions(solutions)
                .build();
        BreedPostInput result = BreedPostInput.builder()
                .withNumSteps(20)
                .withElitismCount(7)
                .withTournamentSize(5)
                .withMutationRate(7.8)
                .withPopulation(population)
                .build();
        return result;
    }
}