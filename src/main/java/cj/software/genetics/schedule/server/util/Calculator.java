package cj.software.genetics.schedule.server.util;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Calculator {
    public double standardDeviation(List<Double> values) {
        double average = average(values);

        double squareSum = 0.0;
        for (double value : values) {
            double delta = value - average;
            double deltaSquare = delta * delta;
            squareSum += deltaSquare;
        }

        double divided = squareSum / values.size();
        double result = Math.sqrt(divided);
        return result;
    }

    public double average(List<Double> values) {
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        double result = sum / values.size();
        return result;
    }
}
