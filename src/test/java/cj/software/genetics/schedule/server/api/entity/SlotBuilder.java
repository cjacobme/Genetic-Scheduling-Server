package cj.software.genetics.schedule.server.api.entity;

public class SlotBuilder extends Slot.Builder {
    public SlotBuilder() {
        super.withPosition(13).withTask(new TaskBuilder().build());
    }
}
