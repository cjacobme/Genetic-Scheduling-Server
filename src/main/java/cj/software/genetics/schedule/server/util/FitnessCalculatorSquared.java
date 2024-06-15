package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.SortedMap;

@Service
public class FitnessCalculatorSquared implements FitnessCalculator {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private Calculator calculator;

    @Override
    public Fitness calculateFitness(Solution solution) {
        List<Worker> workers = solution.getWorkers();
        SortedMap<Integer, Duration> durationsPerPriority = workerService.calculateMaxPerPriority(workers);
        double squared = calculator.squared(durationsPerPriority);
        double fitnessValue = 1.0 / squared;
        Fitness result = Fitness.builder().withFitnessValue(fitnessValue).withRelevantValue(squared).build();
        return result;
    }
}
