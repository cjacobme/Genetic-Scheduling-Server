package cj.software.genetics.schedule.server.api.entity;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SchedulingProblemBuilder extends SchedulingProblem.Builder{
    public SchedulingProblemBuilder() {
        super.withPriorities(List.of(
                new ProblemPriorityBuilder().withValue(1).build(),
                new ProblemPriorityBuilder().withValue(15).withTasks(List.of(
                        Task.builder().withDurationValue(15).withDurationUnit(TimeUnit.DAYS).withIdentifier(414141).build(),
                        new TaskBuilder().withIdentifier(12345).build()
                )).build()
        ));
    }
}
