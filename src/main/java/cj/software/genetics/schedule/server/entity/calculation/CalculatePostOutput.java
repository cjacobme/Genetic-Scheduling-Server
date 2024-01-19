package cj.software.genetics.schedule.server.entity.calculation;

import java.io.Serial;
import java.io.Serializable;

public class CalculatePostOutput implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int result;

    public CalculatePostOutput(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
