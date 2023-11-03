package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionSetup;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

@Service
public class SolutionService {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private RandomService randomService;

    @Autowired
    private TaskService taskService;

    public Solution createInitial(int index, SchedulingProblem schedulingProblem, SolutionSetup solutionSetup) throws SlotOccupiedException {
        int workersCount = solutionSetup.getWorkersPerSolutionCount();
        List<Worker> workers = new ArrayList<>(workersCount);
        for (int iWorker = 0; iWorker < workersCount; iWorker++) {
            Worker worker = workerService.createInitialWorker(schedulingProblem);
            workers.add(worker);
        }
        Solution result = Solution.builder()
                .withIndexInGeneration(index)
                .withGenerationStep(0)
                .withWorkers(workers)
                .build();
        distribute(result, schedulingProblem);
        calculateFitnessValue(result);
        return result;
    }

    Solution distribute(Solution source, SchedulingProblem schedulingProblem) throws SlotOccupiedException {
        Solution result = source;
        List<Worker> workers = result.getWorkers();
        int workersCount = workers.size();
        SortedSet<ProblemPriority> problemPriorities = schedulingProblem.getPriorities();
        for (ProblemPriority problemPriority : problemPriorities) {
            int priorityValue = problemPriority.getValue();
            int slotCount = problemPriority.getSlotCount();
            Collection<Task> tasks = problemPriority.getTasks();
            for (Task task : tasks) {
                int workerIndex = randomService.nextInt(workersCount);
                int slotIndex = randomService.nextInt(slotCount);
                boolean occupied = taskService.isOccupied(result, priorityValue, workerIndex, slotIndex);
                while (occupied) {
                    workerIndex = randomService.nextInt(workersCount);
                    slotIndex = randomService.nextInt(slotCount);
                    occupied = taskService.isOccupied(result, priorityValue, workerIndex, slotIndex);
                }
                taskService.setTaskAt(result, priorityValue, workerIndex, slotIndex, task);
            }
        }
        return result;
    }

    Solution calculateFitnessValue(Solution solution) {
        Solution result = solution;
        long max = -1L;
        List<Worker> workers = solution.getWorkers();
        List<Long> workerDurations = workerService.calculateDurations(workers);
        for (Long duration : workerDurations) {
            max = Math.max(max, duration);
        }
        double fitness = 1.0 / max;
        result.setFitnessValue(fitness);
        return result;
    }
}
