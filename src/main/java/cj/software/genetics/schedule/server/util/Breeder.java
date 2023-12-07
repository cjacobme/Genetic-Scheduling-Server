package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.FitnessProcedure;
import cj.software.genetics.schedule.api.entity.Population;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
import cj.software.util.spring.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class Breeder {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MateService mateService;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private MutationService mutationService;

    @NotNull
    @Valid
    public Population step(
            @Trace
            FitnessProcedure fitnessProcedure,

            Population previous,

            int elitismCount,

            int tournamentSize,
            
            double mutationRate) throws SlotOccupiedException {
        int generationStep = previous.getGenerationStep() + 1;
        List<Solution> previousSolutions = previous.getSolutions();
        int solutionsCount = previousSolutions.size();
        List<Solution> solutions = new ArrayList<>(solutionsCount);
        for (int i = 0; i < elitismCount; i++) {
            solutions.add(previousSolutions.get(i));
        }
        for (int i = elitismCount; i < solutionsCount; i++) {
            Solution parent1 = previousSolutions.get(i);
            Solution parent2 = tournamentService.select(previousSolutions, tournamentSize);
            Solution offspring = mateService.mate(fitnessProcedure, generationStep, i - elitismCount, parent1, parent2);
            mutationService.mutate(offspring, mutationRate);
            solutions.add(offspring);
        }
        solutions = solutionService.sort(solutions);

        Population result = Population.builder()
                .withGenerationStep(generationStep)
                .withSolutions(solutions)
                .build();
        return result;
    }
}
