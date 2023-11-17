package cj.software.genetics.schedule.server.entity.configuration;

import cj.software.genetics.schedule.server.entity.ValidatingTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class GeneralConfigurationTest extends ValidatingTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = GeneralConfiguration.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        GeneralConfiguration.Builder builder = GeneralConfiguration.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                GeneralConfiguration.class);

        GeneralConfiguration instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getBackendVersion()).as("backend version").isNull();
        softy.assertThat(instance.getBackendBuilt()).as("backend built").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        String backendVersion = "_backendVersion";
        String backendBuilt = "_backendBuilt";
        GeneralConfiguration instance = GeneralConfiguration.builder()
                .withBackendVersion(backendVersion)
                .withBackendBuilt(backendBuilt)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getBackendVersion()).as("backend version").isEqualTo(backendVersion);
        softy.assertThat(instance.getBackendBuilt()).as("backend built").isEqualTo(backendBuilt);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        GeneralConfiguration instance = new GeneralConfigurationBuilder().build();
        validate(instance);
    }
}