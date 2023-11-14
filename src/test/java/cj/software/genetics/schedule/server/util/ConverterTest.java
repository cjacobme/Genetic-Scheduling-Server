package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.entity.Coordinate;
import cj.software.genetics.schedule.server.util.spring.BeanProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Converter.class)
class ConverterTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void createObjectMapper() {
        BeanProducer beanProducer = new BeanProducer();
        objectMapper = beanProducer.objectMapper();
    }

    @Autowired
    private Converter converter;

    @Test
    void metadata() {
        Service service = Converter.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void toMapTaskCoordinatePrio1() throws IOException {
        try (InputStream is = Objects.requireNonNull(
                ConverterTest.class.getResourceAsStream("SolutionSample.json"))) {
            Solution solution = objectMapper.readValue(is, Solution.class);
            int priority = 1;
            Map<Task, Coordinate> converted = converter.toMapTaskCoordinate(solution, priority);
            Map<Task, Coordinate> expected = Map.of(
                    Task.builder().withIdentifier(3).build(), Coordinate.builder().withWorkerIndex(1).withSlotIndex(0).build(),
                    Task.builder().withIdentifier(1).build(), Coordinate.builder().withWorkerIndex(1).withSlotIndex(1).build(),
                    Task.builder().withIdentifier(2).build(), Coordinate.builder().withWorkerIndex(1).withSlotIndex(13).build());
            assertThat(converted).as("converted").isEqualTo(expected);
        }
    }

    @Test
    void toMapTaskCoordinate15() throws IOException {
        try (InputStream is = Objects.requireNonNull(
                ConverterTest.class.getResourceAsStream("SolutionSample.json"))) {
            Solution solution = objectMapper.readValue(is, Solution.class);
            int priority = 15;
            Map<Task, Coordinate> converted = converter.toMapTaskCoordinate(solution, priority);
            Map<Task, Coordinate> expected = Map.of(
                    Task.builder().withIdentifier(414141).build(), Coordinate.builder().withWorkerIndex(2).withSlotIndex(10).build(),
                    Task.builder().withIdentifier(12345).build(), Coordinate.builder().withWorkerIndex(4).withSlotIndex(13).build());
            assertThat(converted).as("converted").isEqualTo(expected);
        }
    }

    @Test
    void toTaskListPrio1() throws IOException {
        try (InputStream is = Objects.requireNonNull(
                ConverterTest.class.getResourceAsStream("SolutionSample.json"))) {
            Solution solution = objectMapper.readValue(is, Solution.class);
            int priority = 1;
            List<Task> tasks = converter.toTaskList(solution, priority);
            assertThat(tasks).extracting(Task::getIdentifier).containsExactlyInAnyOrder(1, 2, 3);
        }
    }

    @Test
    void toTaskListPrio15() throws IOException {
        try (InputStream is = Objects.requireNonNull(
                ConverterTest.class.getResourceAsStream("SolutionSample.json"))) {
            Solution solution = objectMapper.readValue(is, Solution.class);
            int priority = 15;
            List<Task> tasks = converter.toTaskList(solution, priority);
            assertThat(tasks).extracting(Task::getIdentifier).containsExactlyInAnyOrder(414141, 12345);
        }

    }
}
