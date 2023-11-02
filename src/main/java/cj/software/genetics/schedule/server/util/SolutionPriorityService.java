package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolutionPriorityService {

    public SolutionPriority createInitial(ProblemPriority problemPriority) {
        List<Task> tasks = createEmpty(problemPriority);
        SolutionPriority result = SolutionPriority.builder()
                .withValue(problemPriority.getValue())
                .withTasks(tasks)
                .build();
        return result;
    }

    private List<Task> createEmpty(ProblemPriority problemPriority) {
        int numSlots = problemPriority.getSlotCount();
        List<Task> result = new ArrayList<>(numSlots);
        for (int iSlot = 0; iSlot < numSlots; iSlot++) {
            result.add(null);
        }
        return result;
    }
}
