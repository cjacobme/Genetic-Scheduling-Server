package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.Solution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TournamentService.class)
class TournamentServiceTest {

    @Autowired
    private TournamentService tournamentService;

    @MockBean
    private SolutionService solutionService;

    @MockBean
    private RandomService randomService;

    @Test
    void metadata() {
        Service service = TournamentService.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void tournament1() {
        List<Solution> solutions = solutions(1.0, 0.1, 100);
        List<Solution> toBeSorted = List.of(
                solutions.get(20),
                solutions.get(31),
                solutions.get(42),
                solutions.get(10),
                solutions.get(16));
        List<Solution> sorted = List.of(
                solutions.get(42),
                solutions.get(31),
                solutions.get(20),
                solutions.get(16),
                solutions.get(10));
        int tournamenSize = 5;

        when(randomService.nextInt(100)).thenReturn(20);
        when(randomService.nextInt(99)).thenReturn(30);
        when(randomService.nextInt(98)).thenReturn(40);
        when(randomService.nextInt(97)).thenReturn(10);
        when(randomService.nextInt(96)).thenReturn(15);
        when(solutionService.sort(toBeSorted)).thenReturn(sorted);

        Solution selected = tournamentService.select(solutions, tournamenSize);

        verify(randomService).nextInt(100);
        verify(randomService).nextInt(99);
        verify(randomService).nextInt(98);
        verify(randomService).nextInt(97);
        verify(randomService).nextInt(96);
        verify(randomService, times(5)).nextInt(anyInt());
        verify(solutionService).sort(toBeSorted);

        assertThat(selected).as("selected").isSameAs(solutions.get(42));
    }

    @Test
    void tournament2() {
        List<Solution> solutions = solutions(10.0, 1.0, 20);
        List<Solution> toBeSorted = List.of(solutions.get(4), solutions.get(3));
        List<Solution> sorted = List.of(solutions.get(0), solutions.get(1));

        when(randomService.nextInt(20)).thenReturn(4);
        when(randomService.nextInt(19)).thenReturn(3);
        when(solutionService.sort(toBeSorted)).thenReturn(sorted);

        Solution selected = tournamentService.select(solutions, 2);

        assertThat(selected).as("selected").isSameAs(solutions.get(0));

    }

    private List<Solution> solutions(double firstFitnessValue, double deltaFitness, int count) {
        double fitnessValue = firstFitnessValue;
        List<Solution> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Solution solution = Solution.builder()
                    .withGenerationStep(0)
                    .withIndexInPopulation(i)
                    .build();
            double duration = 1.0 / fitnessValue;
            Fitness fitness = Fitness.builder().withFitnessValue(fitnessValue).withRelevantValue(duration).build();
            solution.setFitness(fitness);
            result.add(solution);
            fitnessValue += deltaFitness;
        }
        return result;
    }
}
