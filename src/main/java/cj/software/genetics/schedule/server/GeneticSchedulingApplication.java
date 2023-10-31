package cj.software.genetics.schedule.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootConfiguration
@ServletComponentScan
public class GeneticSchedulingApplication {
    public static void main (String[] args) {
        SpringApplication.run(GeneticSchedulingApplication.class, args);
    }
}
