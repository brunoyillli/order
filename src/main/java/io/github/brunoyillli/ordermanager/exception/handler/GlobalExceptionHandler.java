package io.github.brunoyillli.ordermanager.exception.handler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.brunoyillli.ordermanager.dto.error.ErrorTemplate;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorTemplate> handleValidationException(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorTemplate errorTemplate = new ErrorTemplate();
        errorTemplate.setMessage(errors);
        errorTemplate.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorTemplate.setMessage(errors);
        errorTemplate.setTimestamp(LocalDateTime.now().toString());
        return new ResponseEntity<>(errorTemplate, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorTemplate> handleConstraintViolationException(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        ErrorTemplate errorTemplate = new ErrorTemplate();
        errorTemplate.setMessage(errors);
        errorTemplate.setErrorCode(HttpStatus.BAD_REQUEST.value());
        errorTemplate.setMessage(errors);
        errorTemplate.setTimestamp(LocalDateTime.now().toString());
        return new ResponseEntity<>(errorTemplate, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorTemplate> handleIllegalArgumentException(IllegalArgumentException ex) {
    	 ErrorTemplate errorTemplate = new ErrorTemplate();
         errorTemplate.setMessage(ex.getMessage());
         errorTemplate.setErrorCode(HttpStatus.BAD_REQUEST.value());
         errorTemplate.setDetails(ex.getMessage());
         errorTemplate.setTimestamp(LocalDateTime.now().toString());
        return new ResponseEntity<>(errorTemplate, HttpStatus.BAD_REQUEST);
    }
}
