package cj.software.genetics.schedule.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.software"})
public class GeneticSchedulingApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneticSchedulingApplication.class, args);
    }
}
