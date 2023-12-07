package cj.software.genetics.schedule.server.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.within;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Calculator.class)
class CalculatorTest {

    private static final List<Double> VALUES1 = List.of(2.3);

    private static final List<Double> VALUES2 = List.of(3.3, 5.5);

    private static final List<Double> VALUES4 = List.of(3.0, 4.0, 5.0, 6.0);

    private static final List<Double> VALUES5 = List.of(29.0, 21.0, 23.0, 17.0, 20.0);

    private static final List<Double> EMPTY = List.of();

    private static final List<Double> NULL = null;

    @Autowired
    private Calculator calculator;

    @Test
    void metadata() {
        Service service = Calculator.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void average4() {
        average(VALUES4, 4.5);
    }

    @Test
    void average5() {
        average(VALUES5, 22.0);
    }

    @Test
    void averageWithNullThrowsNpe() {
        try {
            calculator.average(NULL);
            fail("expected exception not thrown");
        } catch (NullPointerException expected) {
            String message = expected.getMessage();
            assertThat(message).as("exception message").isEqualTo("null list of values");
        }
    }

    @Test
    void averageWithEmptyThrowsException() {
        failingAverage(EMPTY, "too few entries: 0");
    }

    @Test
    void averageWith1ThrowsException() {
        failingAverage(VALUES1, "too few entries: 1");
    }

    @Test
    void average2() {
        average(VALUES2, 4.4);
    }

    void average(List<Double> values, double expected) {
        double actual = calculator.average(values);
        assertThat(actual).as("calculated average").isEqualTo(expected, within(0.00001));
    }

    void failingAverage(List<Double> values, String expectedMessage) {
        try {
            calculator.average(values);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException expected) {
            String message = expected.getMessage();
            assertThat(message).as("exception message").isEqualTo(expectedMessage);
        }
    }

    @Test
    void standardDeviation0ThrowsException() {
        failingStandardDeviation(EMPTY, "too few entries: 0");
    }

    @Test
    void standardDeviation1ThrowsException() {
        failingStandardDeviation(VALUES1, "too few entries: 1");
    }

    @Test
    void standardDeviation2ThrowsException() {
        failingStandardDeviation(VALUES2, "too few entries: 2");
    }

    @Test
    void standardDeviation4() {
        standardDeviation(VALUES4, 1.11803);
    }

    @Test
    void standardDeviation5() {
        standardDeviation(VALUES5, 4.0);
    }

    private void standardDeviation(List<Double> values, double expected) {
        double actual = calculator.standardDeviation(values);
        assertThat(actual).as("calculated standard deviation").isEqualTo(expected, within(0.00001));
    }

    private void failingStandardDeviation(List<Double> values, String expectedMessage) {
        try {
            calculator.standardDeviation(values);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException expected) {
            String message = expected.getMessage();
            assertThat(message).as("exception message").isEqualTo(expectedMessage);
        }
    }
}
