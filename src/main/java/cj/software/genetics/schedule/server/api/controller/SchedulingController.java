package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.server.api.entity.SchedulingCreatePostInput;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

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
    public void create(
            @RequestBody
            @NotNull
            @Validated
            SchedulingCreatePostInput postInput) {
        //TODO: es gab da sowas wie einen Interceptor, der ConstraintViolatedExceptions zurückliefert
    }
}
