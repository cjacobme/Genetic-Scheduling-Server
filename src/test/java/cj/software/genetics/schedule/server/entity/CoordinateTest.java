package cj.software.genetics.schedule.server.entity;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class CoordinateTest extends ValidatingTest {

    @Test
    void implementsSerializable() {
        Class<?>[] interfaces = Coordinate.class.getInterfaces();
        assertThat(interfaces).as("interfaces").contains(Serializable.class);
    }


    @Test
    void constructEmpty()
            throws NoSuchFieldException,
            SecurityException,
            IllegalArgumentException,
            IllegalAccessException {
        Coordinate.Builder builder = Coordinate.builder();
        assertThat(builder).as("builder").isNotNull();

        Field field = builder.getClass().getDeclaredField("instance");

        Object instanceBefore = field.get(builder);
        assertThat(instanceBefore).as("instance in builder before build").isNotNull().isInstanceOf(
                Coordinate.class);

        Coordinate instance = builder.build();
        assertThat(instance).as("built instance").isNotNull();

        Object instanceAfter = field.get(builder);
        assertThat(instanceAfter).as("instance in builder after build").isNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getWorkerIndex()).as("worker index").isNull();
        softy.assertThat(instance.getSlotIndex()).as("slot index").isNull();
        softy.assertAll();
    }

    @Test
    void constructFilled() {
        Integer workerIndex = 1;
        Integer slotIndex = 2;
        Coordinate instance = Coordinate.builder()
                .withWorkerIndex(workerIndex)
                .withSlotIndex(slotIndex)
                .build();
        assertThat(instance).as("built instance").isNotNull();
        SoftAssertions softy = new SoftAssertions();
        softy.assertThat(instance.getWorkerIndex()).as("worker index").isEqualTo(workerIndex);
        softy.assertThat(instance.getSlotIndex()).as("slot index").isEqualTo(slotIndex);
        softy.assertAll();
    }

    @Test
    void defaultIsValid() {
        Coordinate instance = new CoordinateBuilder().build();
        validate(instance);
    }

    @Test
    void stringPresentation() {
        Coordinate coordinate = new CoordinateBuilder().build();
        String asString = coordinate.toString();
        assertThat(asString).as("String presentation").isEqualTo("Coordinate[12,5]");
    }

    @Test
    void twoEqualObjects() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().build();
        assertThat(instance1).isEqualTo(instance2);
    }

    @Test
    void twoEqualHashes() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void unequalWorkerIndex() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().withWorkerIndex(0).build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalWorkerIndexHashes() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().withWorkerIndex(0).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void unequalSlotIndexes() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().withSlotIndex(0).build();
        assertThat(instance1).isNotEqualTo(instance2);
    }

    @Test
    void unequalSlotIndexHashes() {
        Coordinate instance1 = new CoordinateBuilder().build();
        Coordinate instance2 = new CoordinateBuilder().withSlotIndex(0).build();
        int hash1 = instance1.hashCode();
        int hash2 = instance2.hashCode();
        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    // This test really wants to compare with another object type:
    @SuppressWarnings("squid:S5845")
    void otherObjectType() {
        Coordinate instance = new CoordinateBuilder().build();
        assertThat(instance).isNotEqualTo("hello");
    }
}