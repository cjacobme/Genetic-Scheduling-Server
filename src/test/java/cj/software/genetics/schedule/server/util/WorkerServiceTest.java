package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblemBuilder;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Worker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.SortedSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WorkerService.class)
class WorkerServiceTest {

    @Autowired
    private WorkerService workerService;

    @MockBean
    private SolutionPriorityService solutionPriorityService;

    @Test
    void metadata() {
        Service service = WorkerService.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void createInitial() {
        SchedulingProblem schedulingProblem = new SchedulingProblemBuilder().build();
        SolutionPriority solutionPriority1 = SolutionPriority.builder().withValue(1).build();
        SolutionPriority solutionPriority15 = SolutionPriority.builder().withValue(15).build();

        when(solutionPriorityService.createInitial(any(ProblemPriority.class))).thenReturn(solutionPriority1, solutionPriority15);

        Worker worker = workerService.createInitialWorker(schedulingProblem);

        assertThat(worker).as("worker").isNotNull();
        SortedSet<SolutionPriority> priorities = worker.getPriorities();
        assertThat(priorities).extracting("value").containsExactly(1, 15);
    }
}
