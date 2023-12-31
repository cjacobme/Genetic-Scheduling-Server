package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.FitnessProcedure;
import cj.software.genetics.schedule.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.api.entity.SchedulingProblemBuilder;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.SolutionSetup;
import cj.software.genetics.schedule.api.entity.SolutionSetupBuilder;
import cj.software.genetics.schedule.api.entity.Worker;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InitialSolutionService.class)
class InitialSolutionServiceTest {

    @Autowired
    private InitialSolutionService initialSolutionService;

    @MockBean
    private SolutionService solutionService;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private FitnessCalculatorFactory fitnessCalculatorFactory;

    @Test
    void metadata() throws NoSuchMethodException {
        Service service = InitialSolutionService.class.getAnnotation(Service.class);
        Validated validated = InitialSolutionService.class.getAnnotation(Validated.class);
        Method method = InitialSolutionService.class.getDeclaredMethod(
                "createInitial", int.class, SchedulingProblem.class, SolutionSetup.class);
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
    void createInitial() throws SlotOccupiedException {
        int index = 234;
        SchedulingProblem schedulingProblem = new SchedulingProblemBuilder().build();
        SolutionSetup solutionSetup = new SolutionSetupBuilder().build();
        FitnessCalculator fitnessCalculator = mock(FitnessCalculator.class);
        Worker worker0 = mock(Worker.class);
        Worker worker1 = mock(Worker.class);
        Worker worker2 = mock(Worker.class);
        Worker worker3 = mock(Worker.class);
        Worker worker4 = mock(Worker.class);
        Solution distributed = Solution.builder().build();
        Fitness fitness = Fitness.builder().withFitnessValue(0.1).withRelevantValue(10.0).build();

        when(workerService.createInitialWorker(schedulingProblem))
                .thenReturn(worker0)
                .thenReturn(worker1)
                .thenReturn(worker2)
                .thenReturn(worker3)
                .thenReturn(worker4);
        when(solutionService.distribute(any(Solution.class), same(schedulingProblem))).thenReturn(distributed);
        when(fitnessCalculatorFactory.determineFitnessCalculator(FitnessProcedure.LATEST)).thenReturn(fitnessCalculator);
        when(fitnessCalculator.calculateFitness(distributed)).thenReturn(fitness);

        Solution created = initialSolutionService.createInitial(index, schedulingProblem, solutionSetup);

        assertThat(created).as("created").isNotNull();
        assertThat(created.getFitness()).as("fitness").isSameAs(fitness);

        verify(workerService, times(5)).createInitialWorker(any(SchedulingProblem.class));
        verify(solutionService).distribute(any(Solution.class), any(SchedulingProblem.class));
        verify(fitnessCalculatorFactory).determineFitnessCalculator(FitnessProcedure.LATEST);
        verify(fitnessCalculator).calculateFitness(distributed);
    }
}
