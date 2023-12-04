package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.Worker;
import cj.software.genetics.schedule.server.entity.Fitness;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FitnessCalculatorLatest implements FitnessCalculator {
    private final Logger logger = LogManager.getFormatterLogger();

    @Autowired
    private WorkerService workerService;

    @Override
    public Fitness calculateFitness(Solution solution) {
        long max = -1L;
        List<Worker> workers = solution.getWorkers();
        List<Long> workerDurations = workerService.calculateDurations(workers);
        for (Long workerDuration : workerDurations) {
            max = Math.max(max, workerDuration);
        }
        logger.info("max duration  = %12d", max);
        double durationInSeconds = max;
        double fitnessValue = 1.0 / durationInSeconds;
        Fitness result = Fitness.builder()
                .withDurationInSeconds(durationInSeconds)
                .withFitnessValue(fitnessValue)
                .build();
        return result;
    }
}
