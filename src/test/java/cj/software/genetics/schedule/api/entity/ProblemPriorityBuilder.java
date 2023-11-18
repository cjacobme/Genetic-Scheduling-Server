package cj.software.genetics.schedule.api.entity;

import java.time.Duration;
import java.util.Set;

public class ProblemPriorityBuilder extends ProblemPriority.Builder {
    public ProblemPriorityBuilder() {
        super.withValue(1)
                .withSlotCount(20)
                .withTasks(Set.of(
                        new TaskBuilder().withIdentifier(1).build(),
                        new TaskBuilder().withIdentifier(2).withDuration(Duration.ofSeconds(20)).build(),
                        new TaskBuilder().withIdentifier(3).withDuration(Duration.ofMinutes(1)).build()));
    }
}
