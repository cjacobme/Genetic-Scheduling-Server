package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import cj.software.genetics.schedule.server.entity.Coordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
@Validated
public class MateService {

    @Autowired
    private SolutionPriorityService solutionPriorityService;

    @Autowired
    private Converter converter;

    @Autowired
    private RandomService randomService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private SolutionService solutionService;

    private static final Logger LOGGER = LogManager.getFormatterLogger();

    @NotNull
    @Valid
    public Solution mate(int generationStep, int indexInPopulation, Solution parent1, Solution parent2) {
        SortedSet<SolutionPriority> priorities = solutionPriorityService.determinePriorities(parent1);
        List<Worker> workers = workerService.createEmptyWorkers(parent1);
        for (SolutionPriority solutionPriority : priorities) {
            int prioValue = solutionPriority.getValue();
            Map<Task, Coordinate> converted1 = converter.toMapTaskCoordinate(parent1, prioValue);
            Map<Task, Coordinate> converted2 = converter.toMapTaskCoordinate(parent2, prioValue);
            List<Task> tasks = converter.toTaskList(parent1, prioValue);
            int numTasks = tasks.size();
            int pos1 = randomService.nextInt(numTasks);
            int pos2 = randomService.nextInt(numTasks);
            int lower = Math.min(pos1, pos2);
            int upper = Math.max(pos1, pos2);
            dispatch(tasks, workers, converted1, lower, upper, prioValue);
            dispatch(tasks, workers, converted2, upper, numTasks, prioValue);
            dispatch(tasks, workers, converted2, 0, lower, prioValue);
        }
        Solution result = Solution.builder()
                .withGenerationStep(generationStep)
                .withIndexInPopulation(indexInPopulation)
                .withWorkers(workers)
                .build();
        solutionService.calculateFitnessValue(result);
        return result;
    }

    void dispatch(List<Task> tasks, List<Worker> workers, Map<Task, Coordinate> converted, int lower, int upper, int priorityValue) {
        for (int iTask = lower; iTask < upper; iTask++) {
            Task task = tasks.get(iTask);
            Coordinate coordinate = converted.get(task);
            int workerIndex = coordinate.getWorkerIndex();
            Worker worker = workers.get(workerIndex);
            SolutionPriority solutionPriority = worker.getPriority(priorityValue);
            int slotIndex = coordinate.getSlotIndex();
            Task existing = solutionPriority.getTaskAt(slotIndex);
            int numSlots = 1000;    //TODO wie holen?
            while (existing != null) {
                LOGGER.info("Slot %d of worker %d in prio %d already occupied, try another one...",
                        slotIndex, workerIndex, priorityValue);
                slotIndex++;
                if (slotIndex >= numSlots) {
                    LOGGER.info("reached end, start at 0 now...");
                    slotIndex = 0;
                }
                existing = solutionPriority.getTaskAt(slotIndex);
            }
            solutionPriority.setTaskAt(slotIndex, task);
        }
    }

    public SortedSet<Integer> determinePriorities(Solution solution) {
        List<Worker> workers = solution.getWorkers();
        Worker first = workers.get(0);
        SortedSet<Integer> result = new TreeSet<>();
        for (SolutionPriority priority : first.getPriorities()) {
            int value = priority.getValue();
            result.add(value);
        }
        return result;
    }
}
