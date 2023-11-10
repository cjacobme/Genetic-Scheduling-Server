package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Worker;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
@Validated
public class MateService {

    @NotNull
    @Valid
    public Solution mate(int generationStep, int indexInPopulation, Solution parent1, Solution parent2) {
        return Solution.builder().build();
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
