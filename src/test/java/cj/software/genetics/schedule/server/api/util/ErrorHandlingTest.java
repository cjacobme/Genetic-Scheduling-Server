package cj.software.genetics.schedule.server.api.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.FieldError;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ErrorHandling.class)
class ErrorHandlingTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    @Autowired
    private ErrorHandling errorHandling;

    @Test
    void metadata() {
        Service service = ErrorHandling.class.getAnnotation(Service.class);
        assertThat(service).as("@Service").isNotNull();
    }

    @Test
    void scenario1() {
        FieldError fieldError = new FieldError("person", "name", "asdf", true, null, null, "no null, please");
        String expected = "invalid object: person" + LINE_SEPARATOR
                + "field name: name" + LINE_SEPARATOR
                + "rejected value: asdf" + LINE_SEPARATOR
                + "message: no null, please" + LINE_SEPARATOR + LINE_SEPARATOR;
        String converted = errorHandling.convert(fieldError);
        assertThat(converted).isEqualTo(expected);
    }

    @Test
    void scenario2() {
        FieldError fieldError = new FieldError("bill", "sum", -1, true, null, null, "minimum 0");
        String expected = "invalid object: bill" + LINE_SEPARATOR
                + "field name: sum" + LINE_SEPARATOR
                + "rejected value: -1" + LINE_SEPARATOR
                + "message: minimum 0" + LINE_SEPARATOR + LINE_SEPARATOR;
        String converted = errorHandling.convert(fieldError);
        assertThat(converted).isEqualTo(expected);
    }
}
