package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

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

    public List<Long> calculateDurations(List<Worker> workers) {
        int workersCount = workers.size();
        long[] sums = new long[workersCount];
        for (int iWorker = 0; iWorker < workersCount; iWorker++) {
            Worker worker = workers.get(iWorker);
            SortedSet<SolutionPriority> priorities = worker.getPriorities();
            for (SolutionPriority priority : priorities) {
                List<Task> tasks = priority.getTasks();
                for (Task task : tasks) {
                    if (task != null) {
                        int value = task.getDurationValue();
                        TimeUnit timeUnit = task.getDurationUnit();
                        Duration duration = Duration.of(value, timeUnit.toChronoUnit());
                        long seconds = duration.getSeconds();
                        sums[iWorker] += seconds;
                    }
                }
            }
        }
        List<Long> result = new ArrayList<>(workersCount);
        for (long sum : sums) {
            result.add(sum);
        }
        return result;
    }
}
