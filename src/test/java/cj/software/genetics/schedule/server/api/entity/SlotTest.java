package cj.software.genetics.schedule.server.api.entity;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SlotTest {

    @Test
    void metadata() {
        Class<?>[] interfaces = Slot.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class, Comparable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Slot.Builder builder = Slot.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Slot.class);

        Slot instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getPosition()).as("position").isNull();
        softy.assertThat(instance.getTask()).as("Task").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer position = 1;
        Task task = Task.builder().build();
        Slot instance = Slot.builder()
                .withPosition(position)
                .withTask(task)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getPosition()).as("position").isEqualTo(position);
        softy.assertThat(instance.getTask()).as("task").isSameAs(task);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Slot instance = new SlotBuilder().build();
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Slot>> violations = validator.validate(instance);
            assertThat(violations).as("constraint violations").isEmpty();
        }
    }

    @Test
    void compareNegative() {
        Slot instance1 = new SlotBuilder().withPosition(1).build();
        Slot instance13 = new SlotBuilder().withPosition(13).build();
        int compare = instance1.compareTo(instance13);
        assertThat(compare).isNegative();
    }

    @Test
    void comparePositive() {
        Slot instance1 = new SlotBuilder().withPosition(1).build();
        Slot instance13 = new SlotBuilder().withPosition(13).build();
        int compare = instance13.compareTo(instance1);
        assertThat(compare).isPositive();
    }

    @Test
    void equalObjects() {
        Slot instance1 = new SlotBuilder().build();
        Slot instance2 = new SlotBuilder().withTask(null).build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void equalHashes() {
        Slot instance1 = new SlotBuilder().build();
        Slot instance2 = new SlotBuilder().withTask(null).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalPosition() {
        Slot instance1 = new SlotBuilder().withPosition(1).build();
        Slot instance2 = new SlotBuilder().withPosition(2).build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalPositionHashes() {
        Slot instance1 = new SlotBuilder().withPosition(1).build();
        Slot instance2 = new SlotBuilder().withPosition(2).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void differentObject() {
        Slot instance1 = new SlotBuilder().withPosition(1).build();
        Object instance2 = "asdf";
        assertThat(instance1).isNotEqualTo(instance2);
    }
}