package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.TestTags;
import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.entity.Coordinate;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
import cj.software.util.spring.BeanProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MutationService.class, BeanProducer.class, SolutionPriorityService.class, TaskService.class, Converter.class})
@Tag(TestTags.INTEGRATION_TEST)
class MutationServiceTest {

    @Autowired
    private MutationService mutationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Converter converter;

    @MockBean
    private RandomService randomService;

    @Test
    void metadata() throws NoSuchMethodException {
        Service service = MutationService.class.getAnnotation(Service.class);
        Validated validated = MutationService.class.getAnnotation(Validated.class);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(service).as("@Service").isNotNull();
        softy.assertThat(validated).as("@Validated").isNotNull();
        softy.assertAll();
    }


    /**
     * the tasks are sorted in that order as per their identifiers in prio and workers:
     * <table>
     *     <tr>
     *         <th>Worker</th>
     *         <th>Priority 0</th>
     *         <th>Priority 1</th>
     *     </tr>
     *     <tr>
     *         <th>0</th>
     *         <td>1, 3</td>
     *         <td>6, 7</td>
     *     </tr>
     *     <tr>
     *         <th>1</th>
     *         <td>2, 4</td>
     *         <td>5</td>
     *     </tr>
     * </table>
     * <p>
     * first, we select the priority per random, e.g. 0.
     * <p>
     * Then all tasks of that priority are associated with their coordinates (worker, slot). This gives us:
     * <ul>
     *     <li>1 &rarr; (0, 0)</li>
     *     <li>3 &rarr; (0, 1)</li>
     *     <li>2 &rarr; (1, 0)</li>
     *     <li>4 &rarr; (1, 1)</li>
     * </ul>
     * <p>
     * Then we iterate through these 4. For each, we envoke {@link RandomService#nextDouble()}. If this random value
     * is less than the mutation rate, that task is selected for Mutation. This will happen for the 2nd one with
     * identifier 3.
     * Then another task is selected with
     * {@link RandomService#nextInt(int)} with the value 4. This will be the task with identifier 4. These two tasks
     * swap their position.
     */
    @Test
    void mutate() throws IOException, SlotOccupiedException {
        try (InputStream is = Objects.requireNonNull(MutationServiceTest.class.getResourceAsStream("MateOffspring.json"))) {
            Solution solution = objectMapper.readValue(is, Solution.class);
            int selectedPriority = 0;
            int tasksCount = 4;
            double mutationRate = 0.25;
            when(randomService.nextInt(2)).thenReturn(selectedPriority);
            when(randomService.nextDouble()).thenReturn(0.5, 0.2, 0.6, 0.9);
            when(randomService.nextInt(tasksCount)).thenReturn(3);

            mutationService.mutate(solution, mutationRate);

            verify(randomService).nextInt(2);   // select priority
            verify(randomService, times(tasksCount)).nextDouble();
            verify(randomService).nextInt(4);   // select other task

            Map<Task, Coordinate> tasks = converter.toMapTaskCoordinate(solution, selectedPriority);
            Coordinate coordinate3 = tasks.get(Task.builder().withIdentifier(3).build());
            Coordinate coordinate4 = tasks.get(Task.builder().withIdentifier(4).build());
            Coordinate exp3 = Coordinate.builder().withWorkerIndex(1).withSlotIndex(2).build();
            Coordinate exp4 = Coordinate.builder().withWorkerIndex(0).withSlotIndex(1).build();
            SoftAssertions softy = new SoftAssertions();
            softy.assertThat(coordinate3).as("Coordinate 3").usingRecursiveComparison().isEqualTo(exp3);
            softy.assertThat(coordinate4).as("Coordinate 4").usingRecursiveComparison().isEqualTo(exp4);
            softy.assertAll();
        }
    }
}
