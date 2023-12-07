package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.Fitness;
import cj.software.genetics.schedule.api.entity.Solution;
import cj.software.genetics.schedule.api.entity.Worker;
import org.apache.commons.lang3.ClassUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FitnessCalculatorStdDev.class)
class FitnessCalculatorStdDevTest {

    @Autowired
    private FitnessCalculatorStdDev fitnessCalculatorStdDev;

    @MockBean
    private WorkerService workerService;

    @Test
    void metadata() {
        Service service = FitnessCalculatorStdDev.class.getAnnotation(Service.class);
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(FitnessCalculatorStdDev.class);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(service).as("@Service").isNotNull();
        softy.assertThat(interfaces).contains(FitnessCalculator.class);
        softy.assertAll();
    }

    @Test
    void fiveSecondsInTwoWorkers() {
        Worker worker1 = Worker.builder().build();
        Worker worker2 = Worker.builder().build();
        List<Worker> workers = List.of(worker1, worker2);
        Solution solution = Solution.builder().withWorkers(workers).build();
        List<Long> durations = List.of(1L, 5L);
        double expFitnessValue = 1.0 / 3.0;
        Fitness expected = Fitness.builder().withDurationInSeconds(3.0).withFitnessValue(expFitnessValue).build();

        when(workerService.calculateDurations(workers)).thenReturn(durations);

        Fitness actual = fitnessCalculatorStdDev.calculateFitness(solution);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void fourSecondsIn4Workers() {
        Worker worker1 = Worker.builder().build();
        Worker worker2 = Worker.builder().build();
        Worker worker3 = Worker.builder().build();
        Worker worker4 = Worker.builder().build();
        List<Worker> workers = List.of(worker1, worker2, worker3, worker4);
        Solution solution = Solution.builder().withWorkers(workers).build();
        List<Long> durations = List.of(1L, 2L, 3L, 4L);
        Fitness expected = Fitness.builder().withDurationInSeconds(2.5).withFitnessValue(0.4).build();

        when(workerService.calculateDurations(workers)).thenReturn(durations);

        Fitness actual = fitnessCalculatorStdDev.calculateFitness(solution);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void emptyWorkersThrowException() {
        Solution solution = Solution.builder().build();
        try {
            fitnessCalculatorStdDev.calculateFitness(solution);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException expected) {
            String message = expected.getMessage();
            assertThat(message).as("exception message").isEqualTo("duration sum is 0");
        }
    }

    @Test
    void zeroDurationsNotTakenIntoAccount() {
        Worker worker1 = Worker.builder().build();
        Worker worker2 = Worker.builder().build();
        Worker worker3 = Worker.builder().build();
        List<Worker> workers = List.of(worker1, worker2, worker3);
        Solution solution = Solution.builder().withWorkers(workers).build();
        List<Long> durations = List.of(10L, 0L, 30L);
        Fitness expected = Fitness.builder().withDurationInSeconds(20.0).withFitnessValue(0.05).build();

        when(workerService.calculateDurations(workers)).thenReturn(durations);

        Fitness actual = fitnessCalculatorStdDev.calculateFitness(solution);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void allZerosThrowsException() {
        Worker worker1 = Worker.builder().build();
        Worker worker2 = Worker.builder().build();
        Worker worker3 = Worker.builder().build();
        List<Worker> workers = List.of(worker1, worker2, worker3);
        Solution solution = Solution.builder().withWorkers(workers).build();
        List<Long> durations = List.of(0L, 0L, 0L);

        when(workerService.calculateDurations(workers)).thenReturn(durations);

        try {
            fitnessCalculatorStdDev.calculateFitness(solution);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException expected) {
            String message = expected.getMessage();
            assertThat(message).as("exception message").isEqualTo("duration sum is 0");
        }
    }
}
