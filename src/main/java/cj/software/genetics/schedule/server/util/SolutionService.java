package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
import org.apache.commons.lang3.builder.CompareToBuilder;
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

    public Solution distribute(Solution source, SchedulingProblem schedulingProblem) throws SlotOccupiedException {
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

    public Solution calculateFitnessValue(Solution solution) {
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

    public List<Solution> sort(Collection<Solution> solutions) {
        List<Solution> result = new ArrayList<>(solutions);
        result.sort((solution1, solution2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(solution2.getFitnessValue(), solution1.getFitnessValue());
            int compare = builder.build();
            return compare;
        });
        return result;
    }
}
