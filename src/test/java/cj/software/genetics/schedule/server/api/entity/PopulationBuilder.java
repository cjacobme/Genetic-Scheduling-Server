package cj.software.genetics.schedule.server.api.entity;

import java.util.List;

public class PopulationBuilder extends Population.Builder {
    public PopulationBuilder() {
        super.withGenerationStep(2)
                .withSolutions(List.of(
                        new SolutionBuilder().withGenerationStep(0).withIndexInPopulation(0).build(),
                        new SolutionBuilder().withGenerationStep(15).withIndexInPopulation(22).build(),
                        new SolutionBuilder().withGenerationStep(2).withIndexInPopulation(2).build()
                ));
    }
}
