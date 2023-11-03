package cj.software.genetics.schedule.server.api.entity;

import java.time.Duration;
import java.util.List;

public class SchedulingProblemBuilder extends SchedulingProblem.Builder {
    public SchedulingProblemBuilder() {
        super.withPriorities(List.of(
                new ProblemPriorityBuilder().withValue(1).build(),
                new ProblemPriorityBuilder().withValue(15).withSlotCount(30).withTasks(List.of(
                        Task.builder().withDuration(Duration.ofDays(15)).withIdentifier(414141).build(),
                        new TaskBuilder().withIdentifier(12345).build()
                )).build()
        ));
    }
}
