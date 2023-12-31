package cj.software.genetics.schedule.api.entity;

import java.util.List;

public class SchedulingProblemBuilder extends SchedulingProblem.Builder {
    public SchedulingProblemBuilder() {
        super.withPriorities(List.of(
                new ProblemPriorityBuilder()
                        .withValue(1)
                        .build(),
                new ProblemPriorityBuilder()
                        .withValue(15)
                        .withTasks(List.of(
                                Task.builder()
                                        .withDuration(TimeWithUnit.ofDays(15))
                                        .withIdentifier(414141).build(),
                                new TaskBuilder().withIdentifier(12345).build()
                        )).build()
        ));
    }
}
