package cj.software.genetics.schedule.server.api.entity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

abstract class ValidatingTest {
    private static ValidatorFactory factory;

    private static Validator validator;

    @BeforeAll
    static void createValidation() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void shutdownValidation() {
        factory.close();
    }

    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> result = validator.validate(object, groups);
        return result;
    }
}
