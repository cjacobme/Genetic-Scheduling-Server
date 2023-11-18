package cj.software.genetics.schedule.api.entity;

public class SchedulingCreatePostOutputBuilder extends SchedulingCreatePostOutput.Builder {
    public SchedulingCreatePostOutputBuilder() {
        super.withPopulation(new PopulationBuilder().build());
    }
}
