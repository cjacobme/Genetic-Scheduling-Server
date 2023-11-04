package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import org.springframework.stereotype.Service;

@Service
public class SolutionPriorityService {

    public SolutionPriority createInitial(ProblemPriority problemPriority) {
        SolutionPriority result = SolutionPriority.builder()
                .withValue(problemPriority.getValue())
                .build();
        return result;
    }
}
