package cj.software.genetics.schedule.server.api.entity;

public class SolutionSetupBuilder extends SolutionSetup.Builder {
    public SolutionSetupBuilder() {
        super
                .withSolutionCount(100)
                .withWorkersPerSolutionCount(5);
    }
}
