package cj.software.genetics.schedule.server.util;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RandomService.class)
class RandomServiceTest {

    @Autowired
    private RandomService randomService;

    @Test
    void metadata() {
        Service service = RandomService.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void upTo10() {
        int bound = 10;
        int numGreater0 = 0;
        SoftAssertions softy = new SoftAssertions();
        for (int i = 0; i < 10; i++) {
            int randomNumber = randomService.nextInt(bound);
            if (randomNumber > 0) {
                numGreater0++;
            }
            softy.assertThat(randomNumber).as("random number #%d", i).isGreaterThanOrEqualTo(0).isLessThan(bound);
        }
        softy.assertAll();
        assertThat(numGreater0).isPositive();
    }

    @Test
    void doubleStandard() {
        SoftAssertions softy = new SoftAssertions();
        for (int i = 0; i < 10; i++) {
            double random = randomService.nextDouble();
            if (random < 0.0 || random > 1.0) {
                softy.assertThat(random).as("random #%d", i).isBetween(0.0, 1.0);
            }
        }
        softy.assertAll();
    }

    @Test
    void selectFromIntegerList() {
        List<Integer> list = List.of(0, 1, 2, 3, 4, 5, 6, 7);
        SoftAssertions softy = new SoftAssertions();
        for (int i = 0; i < 10; i++) {
            Integer selected = randomService.nextFrom(list);
            softy.assertThat(selected).as("selected #%d", i).isNotNegative().isLessThanOrEqualTo(7);
        }
        softy.assertAll();
    }

    @Test
    void selectFromStringList() {
        List<String> list = List.of("one", "two", "three");
        SoftAssertions softy = new SoftAssertions();
        for (int i = 0; i < 10; i++) {
            String selected = randomService.nextFrom(list);
            softy.assertThat(selected).as("selected #%d", i).isIn(list);
        }
        softy.assertAll();
    }

    @Test
    void selectFromEmptyList() {
        List<String> list = List.of();
        try {
            randomService.nextFrom(list);
            fail("expected exception not thrown");
        } catch (IllegalArgumentException expected) {
            String message = expected.getMessage();
            assertThat(message).as("exception message").isEqualTo("empty list");
        }
    }
}
