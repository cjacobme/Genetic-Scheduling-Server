package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.api.controller.SchedulingController;
import cj.software.genetics.schedule.api.entity.BreedPostInput;
import cj.software.genetics.schedule.api.entity.BreedPostOutput;
import cj.software.genetics.schedule.api.entity.FitnessCalculated;
import cj.software.genetics.schedule.api.entity.Population;
import cj.software.genetics.schedule.api.entity.SchedulingCreatePostInput;
import cj.software.genetics.schedule.api.entity.SchedulingCreatePostOutput;
import cj.software.genetics.schedule.api.entity.SchedulingProblem;
import cj.software.genetics.schedule.api.entity.SolutionSetup;
import cj.software.genetics.schedule.api.exception.SlotOccupiedException;
import cj.software.genetics.schedule.server.util.Breeder;
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
public class SchedulingControllerImpl implements SchedulingController {

    @Autowired
    private PopulationService populationService;

    @Autowired
    private Breeder breeder;

    @PostMapping(path = "create")
    @NotNull
    @Override
    @Validated(FitnessCalculated.class)
    public SchedulingCreatePostOutput create(
            @RequestBody
            @NotNull
            @Valid
            SchedulingCreatePostInput postInput) throws SlotOccupiedException {
        SchedulingProblem schedulingProblem = postInput.getSchedulingProblem();
        SolutionSetup solutionSetup = postInput.getSolutionSetup();
        Population population = populationService.createInitial(schedulingProblem, solutionSetup);
        SchedulingCreatePostOutput result = SchedulingCreatePostOutput.builder()
                .withPopulation(population)
                .build();
        return result;
    }

    @PostMapping(path = "breed")
    @NotNull
    @Override
    @Validated(FitnessCalculated.class)
    public BreedPostOutput breed(
            @RequestBody
            @NotNull
            @Valid
            BreedPostInput breedPostInput) throws SlotOccupiedException {
        int elitismCount = breedPostInput.getElitismCount();
        int tournamentSize = breedPostInput.getTournamentSize();
        double mutationRate = breedPostInput.getMutationRate();
        Population population = breedPostInput.getPopulation();
        int numCycles = breedPostInput.getNumSteps();
        Population newPopulation = breeder.step(population, elitismCount, tournamentSize, mutationRate);
        for (int i = 1; i < numCycles; i++) {   // one breed cycle already done, so we start the counting with 1 and not with 0
            newPopulation = breeder.step(newPopulation, elitismCount, tournamentSize, mutationRate);
        }
        BreedPostOutput result = BreedPostOutput.builder()
                .withPopulation(newPopulation)
                .build();
        return result;
    }
}
