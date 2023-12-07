package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionPriority;
import cj.software.genetics.schedule.api.entity.Task;
import cj.software.genetics.schedule.api.entity.Worker;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
import cj.software.util.spring.TraceAtLogLevel;
import org.apache.logging.log4j.spi.StandardLevel;
import org.springframework.stereotype.Service;

@Service
@TraceAtLogLevel(level = StandardLevel.DEBUG)
public class TaskService {
    public boolean isOccupied(Solution solution, int priorityValue, int workerIndex, int slotIndex) {
        Worker worker = solution.getWorkers().get(workerIndex);
        SolutionPriority solutionPriority = worker.getPriority(priorityValue);
        Task existing = solutionPriority.getTaskAt(slotIndex);
        boolean result = (existing != null);
        return result;
    }

    public void setTaskAt(
            Solution solution,
            int priorityValue,
            int workerIndex,
            int slotIndex,
            Task task) throws SlotOccupiedException {
        boolean occupied = isOccupied(solution, priorityValue, workerIndex, slotIndex);
        if (occupied) {
            throw new SlotOccupiedException(priorityValue, workerIndex, slotIndex);
        }
        Worker worker = solution.getWorkers().get(workerIndex);
        SolutionPriority solutionPriority = worker.getPriority(priorityValue);
        solutionPriority.setTaskAt(slotIndex, task);
    }

    public void deleteTaskAt(Solution solution, int priorityValue, int workerIndex, int slotIndex) {
        Worker worker = solution.getWorkers().get(workerIndex);
        SolutionPriority solutionPriority = worker.getPriority(priorityValue);
        solutionPriority.setTaskAt(slotIndex, null);
    }
}
