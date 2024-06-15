package cj.software.genetics.schedule.server.util;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

@Service
public class Calculator {
    public double standardDeviation(List<Double> values) {
        checkEntries(values, 3);
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
        checkEntries(values, 2);
        double sum = 0.0;
        for (double value : values) {
            sum += value;
        }
        double result = sum / values.size();
        return result;
    }

    private void checkEntries(List<Double> values, int minSize) {
        if (values == null) {
            throw new NullPointerException("null list of values");
        }
        int numEntries = values.size();
        if (numEntries < minSize) {
            throw new IllegalArgumentException("too few entries: " + numEntries);
        }
    }

    public double squared(SortedMap<Integer, Duration> durationsPerPriority) {
        int count = durationsPerPriority.size();
        int scale = count;
        double sum = 0.0;
        for (Map.Entry<Integer, Duration> durationEntry : durationsPerPriority.entrySet()) {
            long seconds = durationEntry.getValue().toSeconds();
            double secondsSquare = seconds * seconds * (double) scale;
            sum += secondsSquare;
            scale--;
        }
        double result = Math.sqrt(sum);
        return result;
    }
}
