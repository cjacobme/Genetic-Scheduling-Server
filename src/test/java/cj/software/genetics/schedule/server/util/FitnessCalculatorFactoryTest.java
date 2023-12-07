package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.api.entity.FitnessProcedure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FitnessCalculatorFactory.class)
class FitnessCalculatorFactoryTest {

    @Autowired
    private FitnessCalculatorFactory factory;

    @MockBean
    private FitnessCalculatorStdDev stdDev;

    @MockBean
    private FitnessCalculatorLatest latest;

    @Test
    void metadata() {
        Service service = FitnessCalculatorFactory.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void latest() {
        calculator(FitnessProcedure.LATEST, latest);
    }

    @Test
    void stdDev() {
        calculator(FitnessProcedure.STD_DEVIATION, stdDev);
    }

    private void calculator(FitnessProcedure fitnessProcedure, FitnessCalculator expected) {
        FitnessCalculator calculator = factory.determineFitnessCalculator(fitnessProcedure);
        assertThat(calculator).as("calculator").isSameAs(expected);
    }
}
