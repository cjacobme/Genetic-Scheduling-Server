package cj.software.genetics.schedule.server.entity.configuration;

public class GeneralConfigurationBuilder extends GeneralConfiguration.Builder {
    public GeneralConfigurationBuilder() {
        super.withBackendVersion("1.23")
                .withBackendBuilt("2023-11-15T13:14:15+01:00");
    }
}
