package cj.software.genetics.schedule.server.api.entity;

import java.util.concurrent.TimeUnit;

public class TaskBuilder extends Task.Builder{
    public TaskBuilder() {
        super
                .withIdentifier("4243")
                .withDurationValue(10)
                .withDurationUnit(TimeUnit.SECONDS);
    }
}
