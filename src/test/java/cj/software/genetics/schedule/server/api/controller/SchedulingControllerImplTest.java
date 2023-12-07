package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.api.entity.BreedPostInput;
import cj.software.genetics.schedule.api.entity.BreedPostInputBuilder;
import cj.software.genetics.schedule.api.entity.BreedPostOutput;
import cj.software.genetics.schedule.api.entity.FitnessProcedure;
import cj.software.genetics.schedule.api.entity.Population;
import cj.software.genetics.schedule.api.entity.PopulationBuilder;
import cj.software.genetics.schedule.api.entity.SchedulingCreatePostInput;
import cj.software.genetics.schedule.api.entity.SchedulingCreatePostInputBuilder;
import cj.software.genetics.schedule.api.entity.SchedulingCreatePostOutput;
import cj.software.genetics.schedule.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.api.entity.SolutionSetup;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
import cj.software.genetics.schedule.server.util.Breeder;
import cj.software.genetics.schedule.server.util.PopulationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.RestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SchedulingControllerImpl.class)
class SchedulingControllerImplTest {

    @Autowired
    private SchedulingControllerImpl schedulingControllerImpl;

    @MockBean
    private PopulationService populationService;

    @MockBean
    private Breeder breeder;

    @Test
    void metadata() {
        RestController controller = SchedulingControllerImpl.class.getAnnotation(RestController.class);
        assertThat(controller).as("@Controller").isNotNull();
    }

    @Test
    void create() throws SlotOccupiedException {
        SchedulingCreatePostInput schedulingCreatePostInput = new SchedulingCreatePostInputBuilder().build();
        SchedulingProblem schedulingProblem = schedulingCreatePostInput.getSchedulingProblem();
        SolutionSetup solutionSetup = schedulingCreatePostInput.getSolutionSetup();
        Population population = new PopulationBuilder().build();

        when(populationService.createInitial(schedulingProblem, solutionSetup)).thenReturn(population);

        SchedulingCreatePostOutput returned = schedulingControllerImpl.create(schedulingCreatePostInput);

        verify(populationService).createInitial(schedulingProblem, solutionSetup);
        assertThat(returned).as("returned").isNotNull();
        assertThat(returned.getPopulation()).as("returned population").isSameAs(population);
    }

    @Test
    void breed() throws SlotOccupiedException {
        BreedPostInput breedPostInput = new BreedPostInputBuilder().withNumSteps(3).build();
        Population previous = breedPostInput.getPopulation();
        Population breeded1 = Population.builder().build();
        Population breeded2 = Population.builder().build();
        Population breeded3 = Population.builder().build();

        when(breeder.step(FitnessProcedure.AVERAGE, previous, 2, 5, 0.356)).thenReturn(breeded1);
        when(breeder.step(FitnessProcedure.AVERAGE, breeded1, 2, 5, 0.356)).thenReturn(breeded2);
        when(breeder.step(FitnessProcedure.AVERAGE, breeded2, 2, 5, 0.356)).thenReturn(breeded3);

        BreedPostOutput returned = schedulingControllerImpl.breed(breedPostInput);

        verify(breeder).step(FitnessProcedure.AVERAGE, previous, 2, 5, 0.356);
        assertThat(returned).as("returned").isNotNull();
        assertThat(returned.getPopulation()).as("returned population").isSameAs(breeded3);
    }
}
