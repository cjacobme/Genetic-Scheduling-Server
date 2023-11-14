package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Population;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionSetup;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class PopulationService {

    @Autowired
    private InitialSolutionService initialSolutionService;

    public Population createInitial(
            @NotNull
            SchedulingProblem schedulingProblem,

            @NotNull
            SolutionSetup solutionSetup) throws SlotOccupiedException {
        int solutionCount = solutionSetup.getSolutionCount();
        List<Solution> solutions = new ArrayList<>(solutionCount);
        for (int iSolution = 0; iSolution < solutionCount; iSolution++) {
            Solution solution = initialSolutionService.createInitial(iSolution, schedulingProblem, solutionSetup);
            solutions.add(solution);
        }
        solutions.sort((solution1, solution2) -> {
            CompareToBuilder builder = new CompareToBuilder()
                    .append(solution2.getFitnessValue(), solution1.getFitnessValue());
            int result = builder.build();
            return result;
        });

        Population result = Population.builder()
                .withGenerationStep(0)
                .withSolutions(solutions).build();
        return result;
    }
}
