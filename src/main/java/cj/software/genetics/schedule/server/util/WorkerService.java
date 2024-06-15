package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.ProblemPriority;
import cj.software.genetics.schedule.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionPriority;
import cj.software.genetics.schedule.api.entity.Task;
import cj.software.genetics.schedule.api.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

@Service
public class WorkerService {

    @Autowired
    private SolutionPriorityService solutionPriorityService;

    public Worker createInitialWorker(SchedulingProblem schedulingProblem) {
        SortedSet<ProblemPriority> problemPriorities = schedulingProblem.getPriorities();
        Collection<SolutionPriority> solutionPriorities = new ArrayList<>(problemPriorities.size());
        for (ProblemPriority problemPriority : problemPriorities) {
            SolutionPriority solutionPriority = solutionPriorityService.createInitial(problemPriority);
            solutionPriorities.add(solutionPriority);
        }

        Worker result = Worker.builder()
                .withPriorities(solutionPriorities)
                .build();
        return result;
    }

    public List<Worker> createEmptyWorkers(Solution solution) {
        List<Worker> solutionWorkers = solution.getWorkers();
        List<Worker> result = new ArrayList<>(solutionWorkers.size());
        for (Worker solutionWorker : solutionWorkers) {
            Worker newWorker = createEmptyWorker(solutionWorker);
            result.add(newWorker);
        }
        return result;
    }

    Worker createEmptyWorker(Worker solutionWorker) {
        SortedSet<SolutionPriority> solutionPriorities = solutionWorker.getPriorities();
        Collection<SolutionPriority> newPriorities = new ArrayList<>();
        for (SolutionPriority solutionPriorty : solutionPriorities) {
            int prioValue = solutionPriorty.getValue();
            SolutionPriority newPriority = SolutionPriority.builder().withValue(prioValue).build();
            newPriorities.add(newPriority);
        }
        Worker result = Worker.builder().withPriorities(newPriorities).build();
        return result;
    }

    public List<Long> calculateDurations(List<Worker> workers) {
        int workersCount = workers.size();
        long[] sums = new long[workersCount];
        for (int iWorker = 0; iWorker < workersCount; iWorker++) {
            Worker worker = workers.get(iWorker);
            SortedSet<SolutionPriority> priorities = worker.getPriorities();
            for (SolutionPriority priority : priorities) {
                SortedMap<Integer, Task> tasks = priority.getTasks();
                for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                    Task task = entry.getValue();
                    Duration duration = task.getDuration().toDuration();
                    long seconds = duration.toSeconds();
                    sums[iWorker] += seconds;
                }
            }
        }
        List<Long> result = new ArrayList<>(workersCount);
        for (long sum : sums) {
            result.add(sum);
        }
        return result;
    }

    public SortedMap<Integer, Duration> calculateMaxPerPriority(List<Worker> workers) {
        SortedMap<Integer, Duration> result = createInitial(workers);
        for (Worker worker : workers) {
            for (SolutionPriority priority : worker.getPriorities()) {
                long sum = 0L;
                SortedMap<Integer, Task> tasks = priority.getTasks();
                for (Map.Entry<Integer, Task> taskEntry : tasks.entrySet()) {
                    Task task = taskEntry.getValue();
                    Duration duration = task.getDuration().toDuration();
                    long seconds = duration.toSeconds();
                    sum += seconds;
                }
                int prioValue = priority.getValue();
                Duration currentDuration = result.get(prioValue);
                long currentSeconds = currentDuration.toSeconds();
                if (sum > currentSeconds) {
                    Duration newMaxDuration = Duration.ofSeconds(sum);
                    result.put(prioValue, newMaxDuration);
                }
            }
        }
        return result;
    }

    private SortedMap<Integer, Duration> createInitial(List<Worker> workers) {
        SortedMap<Integer, Duration> result = new TreeMap<>();
        Worker first = workers.get(0);
        for (SolutionPriority priority : first.getPriorities()) {
            int value = priority.getValue();
            result.put(value, Duration.ZERO);
        }
        return result;
    }
}
