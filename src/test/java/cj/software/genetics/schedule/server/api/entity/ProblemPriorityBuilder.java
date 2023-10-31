package cj.software.genetics.schedule.server.api.entity;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ProblemPriorityBuilder extends ProblemPriority.Builder{
    public ProblemPriorityBuilder() {
        super.withValue(1)
                .withSlotCount(20)
                .withTasks(Set.of(
                        new TaskBuilder().withIdentifier(1).build(),
                        new TaskBuilder().withIdentifier(2).withDurationValue(20).build(),
                        new TaskBuilder().withIdentifier(3).withDurationValue(1).withDurationUnit(TimeUnit.MINUTES).build()));
    }
}
