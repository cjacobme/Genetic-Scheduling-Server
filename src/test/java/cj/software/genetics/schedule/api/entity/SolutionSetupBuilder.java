package cj.software.genetics.schedule.api.entity;

public class SolutionSetupBuilder extends SolutionSetup.Builder {
    public SolutionSetupBuilder() {
        super
                .withSolutionCount(100)
                .withWorkersPerSolutionCount(5)
                .withFitnessProcedure(FitnessProcedure.LATEST);
    }
}
