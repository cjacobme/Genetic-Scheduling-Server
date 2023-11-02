package cj.software.genetics.schedule.server.exception;

import java.io.Serial;

public class SlotOccupiedException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int priorityValue;

    private final int workerIndex;

    private final int slotIndex;

    public SlotOccupiedException(int priorityValue, int workerIndex, int slotIndex) {
        super(String.format("The slot for priority %d worker %d slot-index %d is already occupied",
                priorityValue, workerIndex, slotIndex));
        this.priorityValue = priorityValue;
        this.workerIndex = workerIndex;
        this.slotIndex = slotIndex;
    }

    public int getPriorityValue() {
        return priorityValue;
    }

    public int getWorkerIndex() {
        return workerIndex;
    }

    public int getSlotIndex() {
        return slotIndex;
    }
}
