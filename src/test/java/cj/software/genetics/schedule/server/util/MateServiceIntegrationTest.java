package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.TestTags;
import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.util.spring.BeanProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MateService.class, SolutionPriorityService.class, Converter.class, WorkerService.class, SolutionService.class, TaskService.class})
@Tag(TestTags.INTEGRATION_TEST)
class MateServiceIntegrationTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void createObjectMapper() {
        BeanProducer beanProducer = new BeanProducer();
        objectMapper = beanProducer.objectMapper();
    }

    @Autowired
    private MateService mateService;

    @MockBean
    private RandomService randomService;

    private Solution readSolution(String fileName) throws IOException {
        try (InputStream is = Objects.requireNonNull(MateServiceIntegrationTest.class.getResourceAsStream(fileName))) {
            Solution result = objectMapper.readValue(is, Solution.class);
            return result;
        }
    }

    @Test
    void mate() throws IOException {
        Solution parent1 = readSolution("MateParent1.json");
        Solution parent2 = readSolution("MateParent2.json");
        Solution expectedOffspring = readSolution("MateOffspring.json");

        when(randomService.nextInt(4)).thenReturn(2, 0);
        when(randomService.nextInt(3)).thenReturn(2, 2);

        Solution offSpring = mateService.mate(222, 3, parent1, parent2);

        assertThat(offSpring).as("offspring").isNotNull();

        verify(randomService, times(2)).nextInt(4);
        verify(randomService, times(2)).nextInt(3);
        verify(randomService, times(4)).nextInt(anyInt());
        assertThat(offSpring).usingRecursiveComparison().isEqualTo(expectedOffspring);
    }

}
