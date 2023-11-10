package cj.software.genetics.schedule.server.api.entity;

public class BreedPostOutputBuilder extends BreedPostOutput.Builder {
    public BreedPostOutputBuilder() {
        super.withPopulation(new PopulationBuilder().withGenerationStep(13).build());
    }
}
