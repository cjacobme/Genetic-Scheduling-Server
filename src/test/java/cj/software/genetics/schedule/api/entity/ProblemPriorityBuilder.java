package cj.software.genetics.schedule.api.entity;

import java.util.Set;

public class ProblemPriorityBuilder extends ProblemPriority.Builder {
    public ProblemPriorityBuilder() {
        super.withValue(1)
                .withTasks(Set.of(
                        new TaskBuilder().withIdentifier(1).build(),
                        new TaskBuilder().withIdentifier(2).withDuration(TimeWithUnit.ofSeconds(20)).build(),
                        new TaskBuilder().withIdentifier(3).withDuration(TimeWithUnit.ofMinutes(1)).build()));
    }
}
