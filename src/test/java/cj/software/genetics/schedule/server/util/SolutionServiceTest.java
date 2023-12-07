package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.ProblemPriority;
import cj.software.genetics.schedule.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.api.entity.SchedulingProblemBuilder;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionBuilder;
import cj.software.genetics.schedule.api.entity.SolutionPriority;
import cj.software.genetics.schedule.api.entity.Task;
import cj.software.genetics.schedule.api.entity.Worker;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                .withWorkers(workers)
                .build();
        solution.setFitness(Fitness.builder().withFitnessValue(2.334).withRelevantValue(0.01424).build());

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
    void sort() {
        Collection<Solution> solutions = new ArrayList<>(List.of(
                createSolution(0, 3.14),
                createSolution(13, 2.95),
                createSolution(6, 12.34)
        ));
        List<Solution> sorted = solutionService.sort(solutions);
        assertThat(sorted).extracting("indexInPopulation").containsExactly(6, 0, 13);
    }

    private Solution createSolution(int indexInPopulation, double fitnessValue) {
        double duration = 1.0 / fitnessValue;
        Solution result = new SolutionBuilder().withIndexInPopulation(indexInPopulation).build();
        Fitness fitness = Fitness.builder().withRelevantValue(duration).withFitnessValue(fitnessValue).build();
        result.setFitness(fitness);
        return result;
    }
}
