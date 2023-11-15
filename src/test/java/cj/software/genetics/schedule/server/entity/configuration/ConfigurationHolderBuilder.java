package cj.software.genetics.schedule.server.entity.configuration;

public class ConfigurationHolderBuilder extends ConfigurationHolder.Builder {
    public ConfigurationHolderBuilder() {
        super.withGeneral(new GeneralConfigurationBuilder().build());
    }
}
