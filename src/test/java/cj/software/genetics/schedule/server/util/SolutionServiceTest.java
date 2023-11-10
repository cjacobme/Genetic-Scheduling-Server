package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblemBuilder;
import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionBuilder;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SolutionService.class)
class SolutionServiceTest {

    @Autowired
    private SolutionService solutionService;

    @MockBean
    private RandomService randomService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private WorkerService workerService;

    @Test
    void distribute() throws SlotOccupiedException {
        int index = 13;
        SchedulingProblem schedulingProblem = new SchedulingProblemBuilder().build();
        List<Worker> workers = createInitialWorkers(schedulingProblem.getPriorities());
        Solution solution = Solution.builder()
                .withIndexInPopulation(index)
                .withGenerationStep(0)
                .withFitnessValue(2.334)
                .withWorkers(workers)
                .build();

        when(randomService.nextInt(2)).thenReturn(0, 0, 0, 1, 1, 0);
        when(randomService.nextInt(20)).thenReturn(1, 1, 0, 18);
        when(randomService.nextInt(30)).thenReturn(5, 9);
        when(taskService.isOccupied(solution, 1, 0, 1)).thenReturn(false, true);
        when(taskService.isOccupied(solution, 1, 0, 0)).thenReturn(false);
        when(taskService.isOccupied(solution, 1, 1, 18)).thenReturn(false);
        when(taskService.isOccupied(solution, 15, 1, 5)).thenReturn(false);
        when(taskService.isOccupied(solution, 15, 0, 9)).thenReturn(false);

        Solution distributed = solutionService.distribute(solution, schedulingProblem);

        assertThat(distributed).as("distributed").isNotNull();
        verify(taskService).setTaskAt(any(Solution.class), eq(1), eq(0), eq(1), any(Task.class));
        verify(taskService).setTaskAt(any(Solution.class), eq(1), eq(0), eq(0), any(Task.class));
        verify(taskService).setTaskAt(any(Solution.class), eq(1), eq(1), eq(18), any(Task.class));
        verify(taskService).setTaskAt(any(Solution.class), eq(15), eq(1), eq(5), any(Task.class));
        verify(taskService).setTaskAt(any(Solution.class), eq(15), eq(0), eq(9), any(Task.class));
        verify(taskService, times(5)).setTaskAt(any(Solution.class), anyInt(), anyInt(), anyInt(), any(Task.class));
    }

    private List<Worker> createInitialWorkers(SortedSet<ProblemPriority> problemPriorities) {
        List<Worker> result = new ArrayList<>();
        for (int iWorker = 0; iWorker < 2; iWorker++) {
            Collection<SolutionPriority> solutionPriorities = createSolutionPriorities(problemPriorities);
            Worker worker = Worker.builder()
                    .withPriorities(solutionPriorities)
                    .build();
            result.add(worker);
        }
        return result;
    }

    private Collection<SolutionPriority> createSolutionPriorities(SortedSet<ProblemPriority> problemPriorities) {
        Collection<SolutionPriority> result = new ArrayList<>();
        SolutionPriority solutionPriority1 = SolutionPriority.builder()
                .withValue(1)
                .build();
        result.add(solutionPriority1);
        SolutionPriority solutionPriority15 = SolutionPriority.builder()
                .withValue(15)
                .build();
        result.add(solutionPriority15);
        return result;
    }

    @Test
    void calculateFitnessValue1() {
        Solution solution = new SolutionBuilder().build();
        List<Long> workerDuratios = List.of(1L, 1L, 2L);
        calculateFitnessValue(solution, workerDuratios, 0.5);
    }

    @Test
    void calculateFitnessValue2() {
        Solution solution = new SolutionBuilder().build();
        List<Long> workerDurations = List.of(1L, 50L, 2L);
        calculateFitnessValue(solution, workerDurations, 0.02);
    }

    private void calculateFitnessValue(Solution solution, List<Long> workerDurations, double expectedFitnessValue) {
        List<Worker> workers = solution.getWorkers();
        when(workerService.calculateDurations(workers)).thenReturn(workerDurations);

        solutionService.calculateFitnessValue(solution);

        assertThat(solution.getFitnessValue()).as("in solution").isEqualTo(expectedFitnessValue, offset(0.00001));
    }


    @Test
    void sort() {
        Collection<Solution> solutions = new ArrayList<>(List.of(
                new SolutionBuilder().withIndexInPopulation(0).withFitnessValue(3.14).build(),
                new SolutionBuilder().withIndexInPopulation(13).withFitnessValue(2.95).build(),
                new SolutionBuilder().withIndexInPopulation(6).withFitnessValue(12.34).build()
        ));
        List<Solution> sorted = solutionService.sort(solutions);
        assertThat(sorted).extracting("indexInPopulation").containsExactly(6, 0, 13);
    }

}
