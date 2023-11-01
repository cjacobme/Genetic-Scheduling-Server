package cj.software.genetics.schedule.server.api.util;

import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

@Service
public class ErrorHandling {
    public String convert(FieldError fieldError) {
        String objectName = fieldError.getObjectName();
        String fieldName = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String message = fieldError.getDefaultMessage();
        String result = String.format("invalid object: %s%nfield name: %s%nrejected value: %s%nmessage: %s%n%n",
                objectName, fieldName, rejectedValue, message);
        return result;
    }
}
