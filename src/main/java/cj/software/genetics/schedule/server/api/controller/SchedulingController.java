package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.server.api.entity.Population;
import cj.software.genetics.schedule.server.api.entity.SchedulingCreatePostInput;
import cj.software.genetics.schedule.server.api.entity.SchedulingCreatePostOutput;
import cj.software.genetics.schedule.server.api.entity.Solution;
import cj.software.genetics.schedule.server.api.entity.SolutionPriority;
import cj.software.genetics.schedule.server.api.entity.Task;
import cj.software.genetics.schedule.server.api.entity.Worker;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(
        path = "schedule",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class SchedulingController {

    @PostMapping(
            path = "create"
    )
    @NotNull
    @Valid
    public SchedulingCreatePostOutput create(
            @RequestBody
            @NotNull
            @Validated
            SchedulingCreatePostInput postInput) {
        Collection<Task> tasks = postInput.getSchedulingProblem().getPriorities().first().getTasks();
        List<Task> tasksList = new ArrayList<>(tasks);
        SchedulingCreatePostOutput result = SchedulingCreatePostOutput.builder()
                .withPopulation(Population.builder()
                        .withSolutions(List.of(
                                Solution.builder()
                                        .withGenerationStep(0)
                                        .withIndexInGeneration(1)
                                        .withFitnessValue(3.14)
                                        .withWorkers(List.of(
                                                Worker.builder()
                                                        .withPriorities(List.of(
                                                                SolutionPriority.builder()
                                                                        .withValue(1)
                                                                        .withTasks(tasksList)
                                                                        .build()
                                                        ))
                                                        .build()
                                        ))
                                        .build()))
                        .build())
                .build();
        return result;
    }
}
