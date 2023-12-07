package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.Solution;

public interface FitnessCalculator {
    Fitness calculateFitness(Solution solution);
}
