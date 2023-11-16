package cj.software.genetics.schedule.server.util;

import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.TaskBuilder;
import cj.software.genetics.schedule.server.api.entity.Worker;
import cj.software.genetics.schedule.server.exception.SlotOccupiedException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TaskService.class)
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    void metadata() {
        Service service = TaskService.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void occupied() {
        Task task = new TaskBuilder().build();
        Solution solution = createSolution(task);
        boolean occupied = taskService.isOccupied(solution, 1, 0, 5);
        assertThat(occupied).as("occupied").isTrue();
    }

    @Test
    void free() {
        Task task = new TaskBuilder().build();
        Solution solution = createSolution(task);
        boolean occupied = taskService.isOccupied(solution, 1, 0, 6);
        assertThat(occupied).as("occupied").isFalse();
    }

    @Test
    void set() throws SlotOccupiedException {
        Solution solution = createSolution(null);
        Task task = new TaskBuilder().build();
        taskService.setTaskAt(solution, 1, 0, 5, task);
        Task found = solution.getWorkers().get(0).getPriority(1).getTasks().get(5);
        assertThat(found).isSameAs(task);
    }

    @Test
    void setAtOccupiedThrowsException() {
        Task task = new TaskBuilder().build();
        Task task2 = new TaskBuilder().withIdentifier(1234543).build();
        Solution solution = createSolution(task);
        try {
            taskService.setTaskAt(solution, 1, 0, 5, task2);
            fail("expected exception not thrown");
        } catch (SlotOccupiedException expected) {
            SoftAssertions softy = new SoftAssertions();
            softy.assertThat(expected.getPriorityValue()).as("Prio value").isEqualTo(1);
            softy.assertThat(expected.getWorkerIndex()).as("worker index").isEqualTo(0);
            softy.assertThat(expected.getSlotIndex()).as("slot index").isEqualTo(5);
            softy.assertAll();
        }
    }

    @Test
    void deleteTaskAt() {
        Task task = new TaskBuilder().build();
        Solution solution = createSolution(task);
        int priorityValue = 1;
        int workerIndex = 0;
        int slotIndex = 5;
        taskService.deleteTaskAt(solution, priorityValue, workerIndex, slotIndex);
        Worker worker = solution.getWorkers().get(workerIndex);
        worker.postLoad();
        SolutionPriority priority = worker.getPriority(priorityValue);
        Task searched = priority.getTaskAt(slotIndex);
        assertThat(searched).isNull();
    }

    private Solution createSolution(Task task) {
        SortedMap<Integer, Task> tasks = new TreeMap<>();
        tasks.put(5, task);
        SolutionPriority solutionPriority = SolutionPriority.builder().withValue(1).withTasks(tasks).build();
        Collection<SolutionPriority> solutionPriorities = List.of(solutionPriority);
        Worker worker = Worker.builder().withPriorities(solutionPriorities).build();
        List<Worker> workers = List.of(worker);
        Solution result = Solution.builder().withGenerationStep(0).withIndexInPopulation(0).withWorkers(workers).build();
        return result;
    }
}
