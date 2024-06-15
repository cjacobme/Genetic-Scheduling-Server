package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.FitnessProcedure;
import cj.software.util.spring.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FitnessCalculatorFactory {

    @Autowired
    private FitnessCalculatorLatest latest;

    @Autowired
    private FitnessCalculatorStdDev stdDev;

    @Autowired
    private FitnessCalculatorSquared squared;

    public FitnessCalculator determineFitnessCalculator(@Trace FitnessProcedure fitnessProcedure) {
        FitnessCalculator result = switch (fitnessProcedure) {
            case LATEST -> latest;
            case STD_DEVIATION -> stdDev;
            case SQUARED -> squared;
        };
        return result;
    }
}
