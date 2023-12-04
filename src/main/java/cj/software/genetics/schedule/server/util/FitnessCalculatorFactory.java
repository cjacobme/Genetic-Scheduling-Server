package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.FitnessProcedure;
import cj.software.util.spring.Trace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FitnessCalculatorFactory {
    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private FitnessCalculatorLatest latest;

    @Autowired
    private FitnessCalculatorAvg avg;

    public FitnessCalculator determineFitnessCalculator(@Trace FitnessProcedure fitnessProcedure) {
        FitnessCalculator result = switch (fitnessProcedure) {
            case LATEST -> latest;
            case AVERAGE -> avg;
        };
        return result;
    }
}
