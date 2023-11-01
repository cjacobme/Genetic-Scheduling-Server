package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.server.api.util.ErrorHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class SchedulingExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ErrorHandling errorHandling;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            String converted = errorHandling.convert(fieldError);
            sb.append(converted);
        }
        String body = sb.toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", MediaType.TEXT_PLAIN_VALUE);
        ResponseEntity<Object> result = new ResponseEntity<>(body, httpHeaders, HttpStatus.BAD_REQUEST);
        return result;
    }


}
