package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MateService.class)
class MateServiceTest {

    @Autowired
    private MateService mateService;

    @MockBean
    private SolutionPriorityService solutionPriorityService;

    @MockBean
    private Converter converter;

    @MockBean
    private RandomService randomService;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private SolutionService solutionService;

    @Test
    void metadata() {
        Service service = MateService.class.getAnnotation(Service.class);
        Validated validate = MateService.class.getAnnotation(Validated.class);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(service).as("@Service").isNotNull();
        softy.assertThat(validate).as("@Validated").isNotNull();
        softy.assertAll();
    }

    @Test
    void determinePriorities1() {
        Task[] tasks = createTasks();
        Solution solution = createParent1(tasks);
        SortedSet<Integer> expected = new TreeSet<>(List.of(0, 1));
        determinePriorities(solution, expected);
    }

    @Test
    void determinePriorities2() {
        SolutionPriority prio13 = SolutionPriority.builder().withValue(13).build();
        SolutionPriority prio27 = SolutionPriority.builder().withValue(27).build();
        SolutionPriority prio31 = SolutionPriority.builder().withValue(31).build();
        SortedSet<SolutionPriority> prios = new TreeSet<>();
        prios.add(prio13);
        prios.add(prio27);
        prios.add(prio31);
        Worker worker = Worker.builder().withPriorities(prios).build();
        List<Worker> workers = List.of(worker);
        Solution solution = Solution.builder().withWorkers(workers).build();
        SortedSet<Integer> expected = new TreeSet<>(List.of(13, 27, 31));
        determinePriorities(solution, expected);
    }

    private void determinePriorities(Solution solution, Set<Integer> expected) {
        SortedSet<Integer> priorities = mateService.determinePriorities(solution);
        assertThat(priorities).as("priorities").isEqualTo(expected);
    }

    private Task[] createTasks() {
        Task[] result = new Task[12];
        result[0] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(1)).build();
        result[1] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(2)).build();
        result[2] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(3)).build();
        result[3] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(4)).build();
        result[4] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(5)).build();
        result[5] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(6)).build();
        result[6] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(7)).build();
        result[7] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(8)).build();
        result[8] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(9)).build();
        result[9] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(10)).build();
        result[10] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(11)).build();
        result[11] = Task.builder().withIdentifier(0).withDuration(Duration.ofMinutes(12)).build();
        return result;
    }

    private Solution createParent1(Task[] tasks) {
        Map<Integer, Task> tasks00 = Map.of(
                4, tasks[0],
                17, tasks[4],
                25, tasks[2]);
        SolutionPriority prio00 = SolutionPriority.builder()
                .withValue(0)
                .withTasks(tasks00)
                .build();
        Map<Integer, Task> tasks01 = Map.of(
                15, tasks[8],
                27, tasks[7]);
        SolutionPriority prio01 = SolutionPriority.builder()
                .withValue(1)
                .withTasks(tasks01)
                .build();
        Collection<SolutionPriority> prios0 = List.of(prio00, prio01);
        Worker worker0 = Worker.builder()
                .withPriorities(prios0)
                .build();

        Map<Integer, Task> tasks10 = Map.of(
                4, tasks[5],
                5, tasks[3]);
        SolutionPriority prio10 = SolutionPriority.builder()
                .withValue(0)
                .withTasks(tasks10)
                .build();
        Map<Integer, Task> tasks11 = Map.of(
                27, tasks[7]);
        SolutionPriority prio11 = SolutionPriority.builder()
                .withValue(1)
                .withTasks(tasks11)
                .build();
        Collection<SolutionPriority> prios1 = List.of(prio10, prio11);
        Worker worker1 = Worker.builder()
                .withPriorities(prios1)
                .build();

        List<Worker> workers = List.of(worker0, worker1);
        Solution result = Solution.builder()
                .withGenerationStep(13)
                .withIndexInPopulation(0)
                .withFitnessValue(4.2)
                .withWorkers(workers)
                .build();
        return result;
    }

    private Solution createParent2(Task[] tasks) {
        Map<Integer, Task> tasks00 = Map.of(
                15, tasks[5],
                25, tasks[0]);
        SolutionPriority prio00 = SolutionPriority.builder()
                .withValue(0)
                .withTasks(tasks00)
                .build();
        Map<Integer, Task> tasks01 = Map.of(
                25, tasks[6],
                42, tasks[8],
                54, tasks[7]);
        SolutionPriority prio01 = SolutionPriority.builder()
                .withValue(1)
                .withTasks(tasks01)
                .build();
        Collection<SolutionPriority> prios0 = List.of(prio00, prio01);
        Worker worker0 = Worker.builder()
                .withPriorities(prios0)
                .build();

        Map<Integer, Task> tasks10 = Map.of(
                24, tasks[1],
                37, tasks[2],
                38, tasks[4],
                52, tasks[3]);
        SolutionPriority prio10 = SolutionPriority.builder()
                .withValue(0)
                .withTasks(tasks10)
                .build();
        SolutionPriority prio11 = SolutionPriority.builder()
                .withValue(1)
                .build();
        Collection<SolutionPriority> prios1 = List.of(prio10, prio11);
        Worker worker1 = Worker.builder()
                .withPriorities(prios1)
                .build();

        List<Worker> workers = List.of(worker0, worker1);
        Solution result = Solution.builder()
                .withGenerationStep(15)
                .withIndexInPopulation(8)
                .withFitnessValue(3.7)
                .withWorkers(workers)
                .build();
        return result;
    }
}
