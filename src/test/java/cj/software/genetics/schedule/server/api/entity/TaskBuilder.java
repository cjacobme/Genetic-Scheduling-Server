package cj.software.genetics.schedule.server.api.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskBuilder extends Task.Builder {
    public static List<Task> create(int startInclusive, int endExclusive) {
        List<Task> result = new ArrayList<>();
        for (int index = startInclusive; index < endExclusive; index++) {
            result.add(new TaskBuilder().withIdentifier(index).build());
        }
        return result;
    }

    public TaskBuilder() {
        super
                .withIdentifier(4243)
                .withDurationValue(10)
                .withDurationUnit(TimeUnit.SECONDS);
    }
}
