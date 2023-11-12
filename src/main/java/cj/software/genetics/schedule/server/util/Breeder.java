package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Population;
import cj.software.genetics.schedule.server.api.entity.Solution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Breeder {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MateService mateService;

    @Autowired
    private SolutionService solutionService;

    public Population step(Population previous, int elitismCount, int tournamentSize) {
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
            Solution offspring = mateService.mate(generationStep, i - elitismCount, parent1, parent2);
            //TODO add mutation
            solutions.add(offspring);
        }
        solutionService.sort(solutions);

        Population result = Population.builder()
                .withGenerationStep(generationStep)
                .withSolutions(solutions)
                .build();
        return result;
    }
}
