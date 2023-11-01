package cj.software.genetics.schedule.server.api.entity;

public class BreedingSetupBuilder extends BreedingSetup.Builder{
    public BreedingSetupBuilder() {
        super.withLoopCount(30);
    }
}
