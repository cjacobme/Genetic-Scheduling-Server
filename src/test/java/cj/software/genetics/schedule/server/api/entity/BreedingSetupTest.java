package cj.software.genetics.schedule.server.api.entity;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BreedingSetupTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = BreedingSetup.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        BreedingSetup.Builder builder = BreedingSetup.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                BreedingSetup.class);

        BreedingSetup instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        assertThat(instance.getLoopCount()).as("loop count").isNull();
    }

    @Test
    void constructFilled() {
        Integer loopCount = -3;
        BreedingSetup instance = BreedingSetup.builder()
                .withLoopCount(loopCount)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        assertThat(instance.getLoopCount()).as("loop count").isEqualTo(loopCount);
    }

    @Test
    void defaultIsValid() {
        BreedingSetup instance = new BreedingSetupBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<BreedingSetup>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }
}