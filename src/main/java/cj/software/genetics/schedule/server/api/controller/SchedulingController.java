package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.server.api.entity.Population;
import cj.software.genetics.schedule.server.api.entity.SchedulingCreatePostInput;
import cj.software.genetics.schedule.server.api.entity.SchedulingCreatePostOutput;
import cj.software.genetics.schedule.server.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.server.api.entity.SolutionSetup;
import cj.software.genetics.schedule.server.util.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(
        path = "schedule",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class SchedulingController {

    @Autowired
    private PopulationService populationService;

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
        SchedulingProblem schedulingProblem = postInput.getSchedulingProblem();
        SolutionSetup solutionSetup = postInput.getSolutionSetup();
        Population population = populationService.createInitial(schedulingProblem, solutionSetup);
        SchedulingCreatePostOutput result = SchedulingCreatePostOutput.builder()
                .withPopulation(population)
                .build();
        return result;
    }
}
