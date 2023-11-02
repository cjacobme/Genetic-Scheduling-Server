package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    public boolean isOccupied(Solution solution, int priorityValue, int workerIndex, int slotIndex) {
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
}
