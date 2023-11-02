package cj.software.genetics.schedule.server.api.entity;

public class SchedulingCreatePostInputBuilder extends SchedulingCreatePostInput.Builder {
    public SchedulingCreatePostInputBuilder() {
        super
                .withSchedulingProblem(new SchedulingProblemBuilder().build())
                .withSolutionSetup(new SolutionSetupBuilder().build());
    }
}
