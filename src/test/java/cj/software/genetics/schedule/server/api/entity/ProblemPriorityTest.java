package cj.software.genetics.schedule.server.api.entity;

import cj.software.genetics.schedule.server.entity.ValidatingTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemPriorityTest extends ValidatingTest {

    @Test
    void metadata() {
        Class<?>[] interfaces = ProblemPriority.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class, Comparable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        ProblemPriority.Builder builder = ProblemPriority.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                ProblemPriority.class);

        ProblemPriority instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getValue()).as("priority value").isNull();
        softy.assertThat(instance.getSlotCount()).as("slot count").isNull();
        softy.assertThat(instance.getTasks()).as("tasks collection").isEmpty();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer value = 13;
        Integer slotCount = 256;
        Collection<Task> tasks = Set.of(
                new TaskBuilder().build(),
                new TaskBuilder().withIdentifier(1).build(),
                new TaskBuilder().withIdentifier(2).build());
        ProblemPriority instance = ProblemPriority.builder()
                .withValue(value)
                .withSlotCount(slotCount)
                .withTasks(tasks)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getValue()).as("value").isEqualTo(value);
        softy.assertThat(instance.getSlotCount()).as("slot count").isEqualTo(slotCount);
        softy.assertThat(instance.getTasks()).as("tasks").containsExactlyElementsOf(tasks);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        ProblemPriority instance = new ProblemPriorityBuilder().build();
        validate(instance);
    }

    @Test
    void stringPresentation() {
        ProblemPriority instance = new ProblemPriorityBuilder().build();
        String asString = instance.toString();
        assertThat(asString).as("String presentation").isEqualTo("ProblemPriority[value=1,slot count=20,task count=3]");
    }

    @Test
    void equalObjects() {
        ProblemPriority instance1 = new ProblemPriorityBuilder().build();
        ProblemPriority instance2 = new ProblemPriorityBuilder().withSlotCount(231542345).withTasks(null).build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void equalHashes() {
        ProblemPriority instance1 = new ProblemPriorityBuilder().build();
        ProblemPriority instance2 = new ProblemPriorityBuilder().withSlotCount(231542345).withTasks(null).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalValues() {
        ProblemPriority instance1 = new ProblemPriorityBuilder().withValue(1).build();
        ProblemPriority instance2 = new ProblemPriorityBuilder().withValue(2).build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalHashes() {
        ProblemPriority instance1 = new ProblemPriorityBuilder().withValue(1).build();
        ProblemPriority instance2 = new ProblemPriorityBuilder().withValue(2).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void unequalDifferentObject() {
        ProblemPriority instance1 = new ProblemPriorityBuilder().build();
        Object instance2 = "other object";
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void compare() {
        ProblemPriority instance1 = new ProblemPriorityBuilder().withValue(4).build();
        ProblemPriority instance2 = new ProblemPriorityBuilder().withValue(2).build();
        int compareResult = instance1.compareTo(instance2);
        assertThat(compareResult).isPositive();
    }
}