package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.FitnessProcedure;
import cj.software.genetics.schedule.api.entity.Population;
import cj.software.genetics.schedule.api.entity.PopulationBuilder;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyCollection;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Breeder.class)
class BreederTest {

    @Autowired
    private Breeder breeder;

    @MockBean
    private TournamentService tournamentService;

    @MockBean
    private MateService mateService;

    @MockBean
    private SolutionService solutionService;

    @MockBean
    private MutationService mutationService;

    @Test
    void metadata() throws NoSuchMethodException {
        Service service = Breeder.class.getAnnotation(Service.class);
        Validated validated = Breeder.class.getAnnotation(Validated.class);
        Method method = Breeder.class.getDeclaredMethod("step", FitnessProcedure.class, Population.class, int.class, int.class, double.class);
        NotNull notNull = method.getAnnotation(NotNull.class);
        Valid valid = method.getAnnotation(Valid.class);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(service).as("@Service").isNotNull();
        softy.assertThat(validated).as("@Validated").isNotNull();
        softy.assertThat(notNull).as("@NotNull at method").isNotNull();
        softy.assertThat(valid).as("@Valid at method").isNotNull();
        softy.assertAll();
    }

    @Test
    void allElitism() throws SlotOccupiedException {
        Population population = new PopulationBuilder().build();
        List<Solution> sorted = List.of(mock(Solution.class), mock(Solution.class), mock(Solution.class));

        when(solutionService.sort(anyCollection())).thenReturn(sorted);

        Population next = breeder.step(FitnessProcedure.STD_DEVIATION, population, 3, 5, 0.4);

        assertThat(next).as("next").isNotNull();
        verify(tournamentService, never()).select(anyList(), anyInt());
        verify(mateService, never()).mate(any(FitnessProcedure.class), anyInt(), anyInt(), any(Solution.class), any(Solution.class));
        verify(solutionService).sort(anyCollection());
        assertThat(next.getSolutions()).containsExactlyElementsOf(sorted);
    }

    @Test
    void oneElitism() throws SlotOccupiedException {
        Population population = new PopulationBuilder().build();
        List<Solution> previousSolutions = population.getSolutions();
        Solution offspring0 = mock(Solution.class);
        Solution offspring1 = mock(Solution.class);
        int tournamentSize = 2;
        int elitismCount = 1;
        List<Solution> sorted = List.of(previousSolutions.get(0), offspring1, offspring0);
        double mutationRate = 0.1;

        when(tournamentService.select(previousSolutions, tournamentSize)).thenReturn(previousSolutions.get(0)).thenReturn(previousSolutions.get(1));
        when(mateService.mate(FitnessProcedure.STD_DEVIATION, 3, 0, previousSolutions.get(1), previousSolutions.get(0))).thenReturn(offspring0);
        when(mateService.mate(FitnessProcedure.STD_DEVIATION, 3, 1, previousSolutions.get(2), previousSolutions.get(1))).thenReturn(offspring1);
        when(solutionService.sort(anyCollection())).thenReturn(sorted);

        Population next = breeder.step(FitnessProcedure.STD_DEVIATION, population, elitismCount, tournamentSize, mutationRate);

        assertThat(next).as("next").isNotNull();
        verify(tournamentService, times(2)).select(anyList(), anyInt());
        verify(mateService, times(2)).mate(any(FitnessProcedure.class), anyInt(), anyInt(), any(Solution.class), any(Solution.class));
        verify(solutionService).sort(anyCollection());
        verify(mutationService, times(2)).mutate(any(Solution.class), eq(mutationRate));
    }
}
