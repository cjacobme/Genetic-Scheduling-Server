package cj.software.genetics.schedule.server.util;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class RandomService {

    private final SecureRandom secureRandom = new SecureRandom();

    public int nextInt(int bound) {
        int result = secureRandom.nextInt(bound);
        return result;
    }
}
