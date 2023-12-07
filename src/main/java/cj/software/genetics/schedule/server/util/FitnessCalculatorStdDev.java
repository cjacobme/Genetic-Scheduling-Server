package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.Worker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FitnessCalculatorStdDev implements FitnessCalculator {
    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private WorkerService workerService;

    @Autowired
    private Converter converter;

    @Autowired
    private Calculator calculator;

    @Override
    public Fitness calculateFitness(Solution solution) {
        List<Worker> workers = solution.getWorkers();
        if (workers.isEmpty()) {
            throw new IllegalArgumentException("empty workers list");
        }
        List<Long> workerDurations = workerService.calculateDurations(workers);
        List<Double> durationsAsDouble = converter.toDoubleList(workerDurations);
        double standardDeviation = calculator.standardDeviation(durationsAsDouble);
        double fitnessValue = 1.0 / standardDeviation;
        logger.info("fitness value = %.12f", fitnessValue);
        Fitness result = Fitness.builder().withDurationInSeconds(standardDeviation).withFitnessValue(fitnessValue).build();
        return result;
    }
}
