package cj.software.genetics.schedule.server.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Calculator.class)
class CalculatorTest {

    private static final List<Double> VALUES1 = List.of(3.0, 4.0, 5.0, 6.0);

    private static final List<Double> VALUES2 = List.of(29.0, 21.0, 23.0, 17.0, 20.0);

    @Autowired
    private Calculator calculator;

    @Test
    void metadata() {
        Service service = Calculator.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void average1() {
        average(VALUES1, 4.5);
    }

    @Test
    void average2() {
        average(VALUES2, 22.0);
    }

    void average(List<Double> values, double expected) {
        double actual = calculator.average(values);
        assertThat(actual).as("calculated average").isEqualTo(expected, within(0.00001));
    }

    @Test
    void standardDeviation1() {
        standardDeviation(VALUES1, 1.11803);
    }

    @Test
    void standardDeviation2() {
        standardDeviation(VALUES2, 4.0);
    }

    private void standardDeviation(List<Double> values, double expected) {
        double actual = calculator.standardDeviation(values);
        assertThat(actual).as("calculated standard deviation").isEqualTo(expected, within(0.00001));
    }
}
