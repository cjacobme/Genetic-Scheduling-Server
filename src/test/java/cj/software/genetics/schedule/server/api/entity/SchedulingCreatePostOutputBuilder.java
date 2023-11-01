package cj.software.genetics.schedule.server.api.entity;

public class SchedulingCreatePostOutputBuilder extends SchedulingCreatePostOutput.Builder {
    public SchedulingCreatePostOutputBuilder() {
        super.withPopulation(new PopulationBuilder().build());
    }
}
