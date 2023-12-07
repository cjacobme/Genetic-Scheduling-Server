package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.Population;
import cj.software.genetics.schedule.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.api.entity.SchedulingProblemBuilder;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionBuilder;
import cj.software.genetics.schedule.api.entity.SolutionSetup;
import cj.software.genetics.schedule.api.entity.SolutionSetupBuilder;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PopulationService.class)
class PopulationServiceTest {

    @Autowired
    private PopulationService populationService;

    @MockBean
    private InitialSolutionService initialSolutionService;

    @Test
    void metadata() {
        Service service = PopulationService.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void createInitial() throws SlotOccupiedException {
        SchedulingProblem schedulingProblem = new SchedulingProblemBuilder().build();
        SolutionSetup solutionSetup = new SolutionSetupBuilder().withSolutionCount(3).build();
        Solution solution0 = createSolution(0, 0.55);
        Solution solution1 = createSolution(1, 0.32);
        Solution solution2 = createSolution(2, 0.13);

        when(initialSolutionService.createInitial(0, schedulingProblem, solutionSetup)).thenReturn(solution0);
        when(initialSolutionService.createInitial(1, schedulingProblem, solutionSetup)).thenReturn(solution1);
        when(initialSolutionService.createInitial(2, schedulingProblem, solutionSetup)).thenReturn(solution2);

        Population population = populationService.createInitial(schedulingProblem, solutionSetup);

        assertThat(population).as("population").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(population.getGenerationStep()).as("generation step").isZero();
        softy.assertThat(population.getSolutions()).as("solutions").containsExactly(solution0, solution1, solution2);
        softy.assertAll();
        verify(initialSolutionService, times(3)).createInitial(anyInt(), any(SchedulingProblem.class), any(SolutionSetup.class));
    }

    private Solution createSolution(int indexInPopulation, double fitnessValue) {
        Solution result = new SolutionBuilder().withIndexInPopulation(indexInPopulation).build();
        Fitness fitness = Fitness.builder().withRelevantValue(1.0 / fitnessValue).withFitnessValue(fitnessValue).build();
        result.setFitness(fitness);
        return result;
    }
}
