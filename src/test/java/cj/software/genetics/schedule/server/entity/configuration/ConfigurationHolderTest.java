package cj.software.genetics.schedule.server.entity.configuration;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationHolderTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = ConfigurationHolder.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void metadata() {
        Configuration configuration = ConfigurationHolder.class.getAnnotation(Configuration.class);
        EnableConfigurationProperties enableConfigurationProperties = ConfigurationHolder.class.getAnnotation(EnableConfigurationProperties.class);
        ConfigurationProperties configurationProperties = ConfigurationHolder.class.getAnnotation(ConfigurationProperties.class);
        Validated validated = ConfigurationHolder.class.getAnnotation(Validated.class);
        EnableAspectJAutoProxy enableAspectJAutoProxy = ConfigurationHolder.class.getAnnotation(EnableAspectJAutoProxy.class);

        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(configuration).as("@Configuration").isNotNull();
        softy.assertThat(enableConfigurationProperties).as("@EnableConfigurationProperties").isNotNull();
        softy.assertThat(configurationProperties).as("@ConfigurationProperties").isNotNull();
        softy.assertThat(validated).as("@Validated").isNotNull();
        softy.assertThat(enableAspectJAutoProxy).as("@EnableAspectJAutoProxy").isNotNull();
        softy.assertAll();
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        ConfigurationHolder.Builder builder = ConfigurationHolder.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                ConfigurationHolder.class);

        ConfigurationHolder instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        assertThat(instance.getGeneral()).as("general").isNull();
    }

    @Test
    void constructFilled() {
        GeneralConfiguration general = GeneralConfiguration.builder().build();
        ConfigurationHolder instance = ConfigurationHolder.builder()
                .withGeneral(general)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        assertThat(instance.getGeneral()).as("general").isSameAs(general);
    }

    @Test
    void defaultIsValid() {
        ConfigurationHolder instance = new ConfigurationHolderBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ConfigurationHolder>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}