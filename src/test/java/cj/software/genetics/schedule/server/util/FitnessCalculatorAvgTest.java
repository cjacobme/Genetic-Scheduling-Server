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
@SpringBootTest(classes = FitnessCalculatorAvg.class)
class FitnessCalculatorAvgTest {

    @Autowired
    private FitnessCalculatorAvg fitnessCalculatorAvg;

    @MockBean
    private WorkerService workerService;

    @Test
    void metadata() {
        Service service = FitnessCalculatorAvg.class.getAnnotation(Service.class);
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(FitnessCalculatorAvg.class);
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

        Fitness actual = fitnessCalculatorAvg.calculateFitness(solution);

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

        Fitness actual = fitnessCalculatorAvg.calculateFitness(solution);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void emptyWorkersThrowException() {
        Solution solution = Solution.builder().build();
        try {
            fitnessCalculatorAvg.calculateFitness(solution);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException expected) {
            String message = expected.getMessage();
            assertThat(message).as("exception message").isEqualTo("duration sum is 0");
        }
    }
}
