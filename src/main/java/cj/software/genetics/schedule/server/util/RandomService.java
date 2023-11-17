package cj.software.genetics.schedule.server.util;

import cj.software.util.spring.Trace;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
public class RandomService {

    private final SecureRandom secureRandom = new SecureRandom();

    @Trace
    public int nextInt(@Trace int bound) {
        int result = secureRandom.nextInt(bound);
        return result;
    }

    @Trace
    public double nextDouble() {
        double result = nextDouble(1.0);
        return result;
    }

    @Trace
    public double nextDouble(@Trace double bound) {
        double result = secureRandom.nextDouble(bound);
        return result;
    }

    @Trace
    public <T> T nextFrom(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("empty list");
        }
        int size = list.size();
        int randomValue = nextInt(size);
        T result = list.get(randomValue);
        return result;
    }
}
