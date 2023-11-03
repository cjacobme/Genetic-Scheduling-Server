package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.ProblemPriority;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblemBuilder;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

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


    /**
     * we have 2 {@link SolutionPriority} objects and 2 {@link Worker} objects. They have tasks with these durations:
     * <table style="border: 1px solid black;">
     *     <tr>
     *         <th style="border: 1px solid black;">Worker index</th>
     *         <th style="border: 1px solid black;">Priority 1</th>
     *         <th style="border: 1px solid black;">Priority 2</th>
     *     </tr>
     *     <tr>
     *         <td valign="top" style="border: 1px solid black;">Worker 1</td>
     *         <td valign="top" style="border: 1px solid black;">
     *             10 seconds<br/>
     *             2 minutes<br/>
     *             30 seconds
     *         </td>
     *         <td valign="top" style="border: 1px solid black;">
     *             12 seconds<br/>
     *             2 minutes
     *         </td>
     *     </tr>
     *     <tr>
     *         <td valign="top" style="border: 1px solid black;">Worker 2</td>
     *         <td valign="top" style="border: 1px solid black;">
     *             40 seconds<br/>
     *             1 minute
     *         </td>
     *         <td valign="top" style="border: 1px solid black;">
     *             45 seconds
     *         </td>
     *     </tr>
     * </table>
     * <p>
     * This will lead to the following sums in seconds for each of these
     * <table style="border: 1px solid black;">
     *     <tr>
     *         <th style="border: 1px solid black;">Worker index</th>
     *         <th style="border: 1px solid black;">Priority 1</th>
     *         <th style="border: 1px solid black;">Priority 2</th>
     *     </tr>
     *     <tr>
     *         <td style="border: 1px solid black;">Worker 1</td>
     *         <td style="border: 1px solid black;">160</td>
     *         <td style="border: 1px solid black;">132</td>
     *     </tr>
     *     <tr>
     *         <td style="border: 1px solid black;">Worker 2</td>
     *         <td style="border: 1px solid black;">100</td>
     *         <td style="border: 1px solid black;">45</td>
     *     </tr>
     * </table>
     * <p>
     * In this table, the rows have to be added. So, this calculation will return a List with 2 integer values, one
     * for each worker. For the 1st worker, the value should be 292, for the 2nd 145.
     */
    @Test
    void calculateDurations() {
        List<Task> prio11Tasks = List.of(
                Task.builder().withIdentifier(1).withDurationValue(10).withDurationUnit(TimeUnit.SECONDS).build(),
                Task.builder().withIdentifier(2).withDurationValue(2).withDurationUnit(TimeUnit.MINUTES).build(),
                Task.builder().withIdentifier(3).withDurationValue(30).withDurationUnit(TimeUnit.SECONDS).build());
        SolutionPriority priority11 = SolutionPriority.builder().withValue(1).withTasks(prio11Tasks).build();
        List<Task> prio12Tasks = List.of(
                Task.builder().withIdentifier(4).withDurationValue(12).withDurationUnit(TimeUnit.SECONDS).build(),
                Task.builder().withIdentifier(5).withDurationValue(2).withDurationUnit(TimeUnit.MINUTES).build());
        SolutionPriority priority12 = SolutionPriority.builder().withValue(2).withTasks(prio12Tasks).build();
        Collection<SolutionPriority> worker1Solutions = List.of(priority11, priority12);
        Worker worker1 = Worker.builder().withPriorities(worker1Solutions).build();

        List<Task> prio21Tasks = List.of(
                Task.builder().withIdentifier(6).withDurationValue(40).withDurationUnit(TimeUnit.SECONDS).build(),
                Task.builder().withIdentifier(7).withDurationValue(1).withDurationUnit(TimeUnit.MINUTES).build());
        SolutionPriority priority21 = SolutionPriority.builder().withValue(1).withTasks(prio21Tasks).build();
        List<Task> prio22Tasks = Collections.singletonList(
                Task.builder().withIdentifier(8).withDurationValue(45).withDurationUnit(TimeUnit.SECONDS).build());
        SolutionPriority priority22 = SolutionPriority.builder().withValue(2).withTasks(prio22Tasks).build();
        Collection<SolutionPriority> worker2Solutions = List.of(priority21, priority22);
        Worker worker2 = Worker.builder().withPriorities(worker2Solutions).build();

        List<Worker> workers = List.of(worker1, worker2);

        List<Integer> durations = workerService.calculateDurations(workers);

        assertThat(durations).as("durations").isEqualTo(List.of(292, 145));
    }
}
