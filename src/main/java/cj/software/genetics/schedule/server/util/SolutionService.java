package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.SolutionSetup;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

@Service
public class SolutionService {

    @Autowired
    private WorkerService workerService;

    private final SecureRandom secureRandom = new SecureRandom();

    public Solution createInitial(int index, SchedulingProblem schedulingProblem, SolutionSetup solutionSetup) {
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
                .withFitnessValue(2.334)
                .build();
        distribute(result, schedulingProblem);
        return result;
    }

    private Solution distribute(Solution source, SchedulingProblem schedulingProblem) {
        Solution result = source;
        List<Worker> workers = result.getWorkers();
        int workersCount = workers.size();
        SortedSet<ProblemPriority> problemPriorities = schedulingProblem.getPriorities();
        for (ProblemPriority problemPriority : problemPriorities) {
            int priorityValue = problemPriority.getValue();
            int slotCount = problemPriority.getSlotCount();
            Collection<Task> tasks = problemPriority.getTasks();
            for (Task task : tasks) {
                int workerIndex = secureRandom.nextInt(workersCount);
                int slotIndex = secureRandom.nextInt(slotCount);
                boolean occupied = isOccupied(result, priorityValue, workerIndex, slotIndex);
                while (occupied) {
                    workerIndex = secureRandom.nextInt(workersCount);
                    slotIndex = secureRandom.nextInt(slotCount);
                    occupied = isOccupied(result, priorityValue, workerIndex, slotIndex);
                }
                set(result, priorityValue, workerIndex, slotIndex, task);
            }
        }
        return result;
    }

    private boolean isOccupied(Solution solution, int priorityValue, int workerIndex, int slotIndex) {
        Worker worker = solution.getWorkers().get(workerIndex);
        SolutionPriority solutionPriority = worker.getPriority(priorityValue);
        List<Task> tasks = solutionPriority.getTasks();
        int tasksCount = tasks.size();
        boolean result;
        if (slotIndex < tasksCount) {
            Task occupied = tasks.get(slotIndex);
            result = (occupied != null);
        } else {
            result = false;
        }
        return result;
    }

    private void set(Solution solution, int priorityValue, int workerIndex, int slotIndex, Task task) {
        Worker worker = solution.getWorkers().get(workerIndex);
        SolutionPriority solutionPriority = worker.getPriority(priorityValue);
        solutionPriority.setTaskAt(slotIndex, task);

    }
}
