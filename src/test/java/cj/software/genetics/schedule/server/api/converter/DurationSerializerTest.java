package cj.software.genetics.schedule.server.api.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DurationSerializerTest {

    private final DurationSerializer serializer = new DurationSerializer();

    @Test
    void nullSerializesNull() throws IOException {
        check(null, null);
    }

    private void check(Duration duration, String expected) throws IOException {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(duration, jsonGenerator, provider);

        verify(jsonGenerator).writeString(expected);

    }

    @Test
    void tenSeconds() throws IOException {
        check(Duration.ofSeconds(10), "PT10S");
    }

    @Test
    void fiveMinutes() throws IOException {
        check(Duration.ofMinutes(5), "PT5M");
    }
}
