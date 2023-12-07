package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.FitnessCalculated;
import cj.software.genetics.schedule.api.entity.FitnessProcedure;
import cj.software.genetics.schedule.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionSetup;
import cj.software.genetics.schedule.api.entity.Worker;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class InitialSolutionService {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private FitnessCalculatorFactory fitnessCalculatorFactory;

    @NotNull
    @Valid
    @Validated(FitnessCalculated.class)
    public Solution createInitial(int index, SchedulingProblem schedulingProblem, SolutionSetup solutionSetup) throws SlotOccupiedException {
        int workersCount = solutionSetup.getWorkersPerSolutionCount();
        List<Worker> workers = new ArrayList<>(workersCount);
        for (int iWorker = 0; iWorker < workersCount; iWorker++) {
            Worker worker = workerService.createInitialWorker(schedulingProblem);
            workers.add(worker);
        }
        Solution result = Solution.builder()
                .withIndexInPopulation(index)
                .withGenerationStep(0)
                .withWorkers(workers)
                .build();
        result = solutionService.distribute(result, schedulingProblem);
        FitnessProcedure fitnessProcedure = solutionSetup.getFitnessProcedure();
        FitnessCalculator fitnessCalculator = fitnessCalculatorFactory.determineFitnessCalculator(fitnessProcedure);
        Fitness fitness = fitnessCalculator.calculateFitness(result);
        result.setFitness(fitness);
        return result;
    }
}
