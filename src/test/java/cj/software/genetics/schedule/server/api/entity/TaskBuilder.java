package cj.software.genetics.schedule.server.api.entity;

import java.util.concurrent.TimeUnit;

public class TaskBuilder extends Task.Builder{
    public TaskBuilder() {
        super.withDurationValue(10)
                .withDurationUnit(TimeUnit.SECONDS);
    }
}
