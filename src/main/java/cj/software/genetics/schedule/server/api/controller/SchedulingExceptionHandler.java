package cj.software.genetics.schedule.server.api.controller;

import cj.software.genetics.schedule.server.api.util.ErrorHandling;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@ControllerAdvice
public class SchedulingExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ErrorHandling errorHandling;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleJavaLangException(
            Exception exception,
            @SuppressWarnings("unused") WebRequest webRequest) throws IOException {
        ResponseEntity<Object> result = report(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        return result;
    }

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

    private ResponseEntity<Object> report(Exception exception, HttpStatus httpStatus) throws IOException {
        Logger myLogger = LogManager.getFormatterLogger();
        myLogger.error("exception occured", exception);
        String message = constructStackTrace(exception);
        ResponseEntity<Object> result = report(message, httpStatus);
        return result;
    }

    private String constructStackTrace(Exception exception) throws IOException {
        try (StringWriter sw = new StringWriter()) {
            try (PrintWriter pw = new PrintWriter(sw)) {
                exception.printStackTrace(pw);
            }
            String stackTrace = sw.toString();

            StringBuilder sb = new StringBuilder();
            String lineSep = System.getProperty("line.separator");
            sb.append(exception.getClass().getSimpleName()).append(": ").append(exception.getMessage());
            sb.append(lineSep).append(lineSep).append(stackTrace);
            String result = sb.toString();
            return result;
        }
    }

    private ResponseEntity<Object> report(String message, HttpStatus httpStatus) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", MediaType.TEXT_PLAIN_VALUE);
        ResponseEntity<Object> result = new ResponseEntity<>(message, httpHeaders, httpStatus);
        return result;
    }

}
