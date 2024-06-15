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

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FitnessCalculatorSquared.class)
class FitnessCalculatorSquaredTest {
    @Autowired
    private FitnessCalculatorSquared fitnessCalculatorSquared;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private Calculator calculator;

    @Test
    void metadata() {
        Service service = FitnessCalculatorSquared.class.getAnnotation(Service.class);
        List<Class<?>> interfaces = ClassUtils.getAllInterfaces(FitnessCalculatorSquared.class);
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(service).as("@Service").isNotNull();
        softy.assertThat(interfaces).as("interfaces").contains(FitnessCalculator.class);
        softy.assertAll();
    }

    @Test
    void twoPriorities() {
        Worker worker1 = Worker.builder().build();
        Worker worker2 = Worker.builder().build();
        List<Worker> workers = List.of(worker1, worker2);
        Solution solution = Solution.builder().withWorkers(workers).build();
        SortedMap<Integer, Duration> durationsPerPriority = new TreeMap<>(Map.of(1, Duration.ofSeconds(160), 2, Duration.ofSeconds(132)));
        double squared = 100.0;

        when(workerService.calculateMaxPerPriority(workers)).thenReturn(durationsPerPriority);
        when(calculator.squared(durationsPerPriority)).thenReturn(squared);

        Fitness actual = fitnessCalculatorSquared.calculateFitness(solution);

        assertThat(actual).as("returned fitness").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(actual.getFitnessValue()).as("fitness value").isEqualTo(0.01, within(0.00001));
        softy.assertThat(actual.getRelevantValue()).as("relevant value").isEqualTo(squared, within(0.000001));
        softy.assertAll();
    }
}
