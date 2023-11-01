package cj.software.genetics.schedule.server.api.entity;

import java.util.List;

public class SolutionBuilder extends Solution.Builder {
    public SolutionBuilder() {
        super.withGenerationStep(15)
                .withIndexInGeneration(33)
                .withFitnessValue(2.35746)
                .withWorkers(List.of(
                        new WorkerBuilder().build(),
                        new WorkerBuilder().build(),
                        new WorkerBuilder().build()
                ));
    }
}
