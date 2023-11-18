package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.ProblemPriority;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionPriority;
import cj.software.genetics.schedule.api.entity.Worker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
public class SolutionPriorityService {

    public SolutionPriority createInitial(ProblemPriority problemPriority) {
        SolutionPriority result = SolutionPriority.builder()
                .withValue(problemPriority.getValue())
                .build();
        return result;
    }

    public SortedSet<SolutionPriority> determinePriorities(Solution solution) {
        SortedSet<SolutionPriority> result = new TreeSet<>();
        List<Worker> workers = solution.getWorkers();
        for (Worker worker : workers) {
            result.addAll(worker.getPriorities());
        }
        return result;
    }
}
