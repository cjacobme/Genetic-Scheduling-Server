package cj.software.genetics.schedule.api.entity;

import java.util.List;

public class SolutionBuilder extends Solution.Builder {
    public SolutionBuilder() {
        super.withGenerationStep(15)
                .withIndexInPopulation(33)
                .withWorkers(List.of(
                        new WorkerBuilder().build(),
                        new WorkerBuilder().build(),
                        new WorkerBuilder().build()
                ));
    }
}
