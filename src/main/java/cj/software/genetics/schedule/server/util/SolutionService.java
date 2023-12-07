package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.ProblemPriority;
import cj.software.genetics.schedule.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.Task;
import cj.software.genetics.schedule.api.entity.Worker;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
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

    public List<Solution> sort(Collection<Solution> solutions) {
        List<Solution> result = new ArrayList<>(solutions);
        result.sort((solution1, solution2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(solution2.getFitness().getFitnessValue(), solution1.getFitness().getFitnessValue());
            int compare = builder.build();
            return compare;
        });
        return result;
    }
}
