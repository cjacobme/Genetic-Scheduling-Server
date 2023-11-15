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
}
