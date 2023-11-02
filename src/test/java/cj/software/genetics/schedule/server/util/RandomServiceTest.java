package cj.software.genetics.schedule.server.util;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

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
}
