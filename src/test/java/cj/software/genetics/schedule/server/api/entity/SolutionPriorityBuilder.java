package cj.software.genetics.schedule.server.api.entity;

public class SolutionPriorityBuilder extends SolutionPriority.Builder {
    public SolutionPriorityBuilder() {
        super.withValue(1).withTasks(TaskBuilder.create(15, 35));
    }
}
