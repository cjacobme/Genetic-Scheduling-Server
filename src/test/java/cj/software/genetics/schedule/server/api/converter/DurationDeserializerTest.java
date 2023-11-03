package cj.software.genetics.schedule.server.api.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DurationDeserializerTest {
    private final DurationDeserializer durationDeserializer = new DurationDeserializer();

    private void deserialize(String text, Duration expected) throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        JsonNode jsonNode = mock(JsonNode.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getCodec()).thenReturn(objectCodec);
        when(objectCodec.readTree(jsonParser)).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn(text);

        Duration converted = durationDeserializer.deserialize(jsonParser, deserializationContext);

        assertThat(converted).isEqualTo(expected);
        verify(jsonParser).getCodec();
        verify(objectCodec).readTree(jsonParser);
        verify(jsonNode).asText();
    }

    @Test
    void nullParsesNull() throws IOException {
        deserialize(null, null);
    }

    @Test
    void tenSeconds() throws IOException {
        deserialize("PT10S", Duration.ofSeconds(10));
    }
}
