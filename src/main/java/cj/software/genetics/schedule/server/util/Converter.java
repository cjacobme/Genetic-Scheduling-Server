package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionPriority;
import cj.software.genetics.schedule.api.entity.Task;
import cj.software.genetics.schedule.api.entity.Worker;
import cj.software.genetics.schedule.server.entity.Coordinate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Service
public class Converter {
    public Map<Task, Coordinate> toMapTaskCoordinate(Solution solution, int priority) {
        Map<Task, Coordinate> result = new HashMap<>();
        List<Worker> workers = solution.getWorkers();
        int workerIndex = 0;
        for (Worker worker : workers) {
            SolutionPriority solutionPriority = worker.getPriority(priority);
            SortedMap<Integer, Task> tasks = solutionPriority.getTasks();
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                int slotIndex = entry.getKey();
                Coordinate coordinate = Coordinate.builder().withWorkerIndex(workerIndex).withSlotIndex(slotIndex).build();
                Task task = entry.getValue();
                result.put(task, coordinate);
            }
            workerIndex++;
        }
        return result;
    }

    public List<Task> toTaskList(Solution solution, int priority) {
        List<Task> result = new ArrayList<>();
        List<Worker> workers = solution.getWorkers();
        for (Worker worker : workers) {
            SolutionPriority solutionPriority = worker.getPriority(priority);
            SortedMap<Integer, Task> tasks = solutionPriority.getTasks();
            result.addAll(tasks.values());
        }
        return result;
    }
}
