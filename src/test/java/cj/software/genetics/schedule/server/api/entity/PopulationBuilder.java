package cj.software.genetics.schedule.server.api.entity;

import java.util.List;

public class PopulationBuilder extends Population.Builder {
    public PopulationBuilder() {
        super.withGenerationStep(2)
                .withSolutions(List.of(
                        new SolutionBuilder().withGenerationStep(0).withIndexInGeneration(0).build(),
                        new SolutionBuilder().withGenerationStep(15).withIndexInGeneration(22).build(),
                        new SolutionBuilder().withGenerationStep(2).withIndexInGeneration(2).build()
                ));
    }
}
