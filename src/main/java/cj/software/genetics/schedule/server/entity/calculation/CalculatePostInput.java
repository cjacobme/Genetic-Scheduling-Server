package cj.software.genetics.schedule.server.entity.calculation;

import io.swagger.annotations.ApiModel;

import java.io.Serial;
import java.io.Serializable;

@ApiModel(description = "input data for a calculation")
public class CalculatePostInput implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int number1;

    private int number2;

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }
}
