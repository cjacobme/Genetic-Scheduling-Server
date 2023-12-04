package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.server.entity.Fitness;

public interface FitnessCalculator {
    Fitness calculateFitness(Solution solution);
}
