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
public class FitnessCalculatorAvg implements FitnessCalculator {
    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private WorkerService workerService;

    @Override
    public Fitness calculateFitness(Solution solution) {
        List<Worker> workers = solution.getWorkers();
        List<Long> workerDurations = workerService.calculateDurations(workers);
        long sum = 0L;
        for (Long workerDuration : workerDurations) {
            sum += workerDuration;
        }
        if (0 == sum) {
            throw new IllegalArgumentException("duration sum is 0");
        }
        logger.info("duration sum = %12d", sum);
        double durationInSeconds = (double) sum / workers.size();
        double fitnessValue = 1.0 / durationInSeconds;
        logger.info("fitness value = %.12f", fitnessValue);
        Fitness result = Fitness.builder().withDurationInSeconds(durationInSeconds).withFitnessValue(fitnessValue).build();
        return result;
    }
}
