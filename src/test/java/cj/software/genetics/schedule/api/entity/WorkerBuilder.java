package cj.software.genetics.schedule.api.entity;

import java.util.List;

public class WorkerBuilder extends Worker.Builder {
    public WorkerBuilder() {
        super.withPriorities(List.of(
                new SolutionPriorityBuilder().build(),
                new SolutionPriorityBuilder().withValue(2).build()
        ));
    }
}
