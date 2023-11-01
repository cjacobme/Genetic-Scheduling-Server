package cj.software.genetics.schedule.server.api.entity;

import java.util.List;

public class WorkerBuilder extends Worker.Builder {
    public WorkerBuilder() {
        super.withPriorities(List.of(
                new SolutionPriorityBuilder().build(),
                new SolutionPriorityBuilder().withValue(2).build()
        ));
    }
}
