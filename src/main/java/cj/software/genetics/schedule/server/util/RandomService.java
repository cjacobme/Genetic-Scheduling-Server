package cj.software.genetics.schedule.server.util;

import cj.software.util.spring.Trace;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

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
}
