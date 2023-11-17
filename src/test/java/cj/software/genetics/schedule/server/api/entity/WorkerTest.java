package cj.software.genetics.schedule.server.api.entity;

import cj.software.genetics.schedule.server.entity.ValidatingTest;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WorkerTest extends ValidatingTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Worker.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }

    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Worker.Builder builder = Worker.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Worker.class);

        Worker instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        assertThat(instance.getPriorities()).isEmpty();
    }

    @Test
    void constructFilled() {
        SolutionPriority prio1 = new SolutionPriorityBuilder().build();
        SolutionPriority prio25 = new SolutionPriorityBuilder().withValue(25).build();
        SolutionPriority prio3 = new SolutionPriorityBuilder().withValue(3).build();
        Collection<SolutionPriority> priorities = List.of(prio1, prio25, prio3);
        Worker instance = Worker.builder()
                .withPriorities(priorities)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        assertThat(instance.getPriorities()).as("priorities")
                .extracting("value")
                .containsExactly(1, 3, 25);
    }

    @Test
    void defaultIsValid() {
        Worker instance = new WorkerBuilder().build();
        validate(instance);
    }
}