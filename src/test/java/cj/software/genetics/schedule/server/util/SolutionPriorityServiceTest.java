package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.ProblemPriorityBuilder;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SolutionPriorityService.class)
class SolutionPriorityServiceTest {

    @Autowired
    private SolutionPriorityService solutionPriorityService;

    @Test
    void metadata() {
        Service service = SolutionPriorityService.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void createInitial() {
        ProblemPriority problemPriority = new ProblemPriorityBuilder().build();
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tasks.add(null);
        }

        SolutionPriority actual = solutionPriorityService.createInitial(problemPriority);
        SolutionPriority expected = SolutionPriority.builder()
                .withValue(1)
                .withTasks(tasks)
                .build();
        assertThat(actual).as("created").usingRecursiveComparison().isEqualTo(expected);
    }
}
