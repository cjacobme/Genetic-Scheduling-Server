package cj.software.genetics.schedule.api.entity;

public class BreedPostInputBuilder extends BreedPostInput.Builder {
    public BreedPostInputBuilder() {
        super
                .withFitnessProcedure(FitnessProcedure.STD_DEVIATION)
                .withNumSteps(10)
                .withElitismCount(2)
                .withTournamentSize(5)
                .withMutationRate(0.356)
                .withPopulation(new PopulationBuilder().build());
    }
}
