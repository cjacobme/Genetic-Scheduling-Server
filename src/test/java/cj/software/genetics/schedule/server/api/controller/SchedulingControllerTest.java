package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.server.api.entity.BreedPostInput;
import cj.software.genetics.schedule.server.api.entity.BreedPostInputBuilder;
import cj.software.genetics.schedule.server.api.entity.BreedPostOutput;
import cj.software.genetics.schedule.server.api.entity.Population;
import cj.software.genetics.schedule.server.api.entity.PopulationBuilder;
import cj.software.genetics.schedule.server.api.entity.SchedulingCreatePostInput;
import cj.software.genetics.schedule.server.api.entity.SchedulingCreatePostInputBuilder;
import cj.software.genetics.schedule.server.api.entity.SchedulingCreatePostOutput;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.SolutionSetup;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
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
@SpringBootTest(classes = SchedulingController.class)
class SchedulingControllerTest {

    @Autowired
    private SchedulingController schedulingController;

    @MockBean
    private PopulationService populationService;

    @MockBean
    private Breeder breeder;

    @Test
    void metadata() {
        RestController controller = SchedulingController.class.getAnnotation(RestController.class);
        assertThat(controller).as("@Controller").isNotNull();
    }

    @Test
    void create() throws SlotOccupiedException {
        SchedulingCreatePostInput schedulingCreatePostInput = new SchedulingCreatePostInputBuilder().build();
        SchedulingProblem schedulingProblem = schedulingCreatePostInput.getSchedulingProblem();
        SolutionSetup solutionSetup = schedulingCreatePostInput.getSolutionSetup();
        Population population = new PopulationBuilder().build();

        when(populationService.createInitial(schedulingProblem, solutionSetup)).thenReturn(population);

        SchedulingCreatePostOutput returned = schedulingController.create(schedulingCreatePostInput);

        verify(populationService).createInitial(schedulingProblem, solutionSetup);
        assertThat(returned).as("returned").isNotNull();
        assertThat(returned.getPopulation()).as("returned population").isSameAs(population);
    }

    @Test
    void breed() {
        BreedPostInput breedPostInput = new BreedPostInputBuilder().build();
        Population previous = breedPostInput.getPopulation();
        Population breeded = Population.builder().build();

        when(breeder.step(previous, 2, 5)).thenReturn(breeded);

        BreedPostOutput returned = schedulingController.breed(breedPostInput);

        verify(breeder).step(previous, 2, 5);
        assertThat(returned).as("returned").isNotNull();
        assertThat(returned.getPopulation()).as("returned population").isSameAs(breeded);
    }
}
