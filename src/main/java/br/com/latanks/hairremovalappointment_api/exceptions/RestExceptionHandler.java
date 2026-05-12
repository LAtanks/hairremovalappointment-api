package br.com.latanks.hairremovalappointment_api.exceptions;

import br.com.latanks.hairremovalappointment_api.exceptions.impl.InvalidCredentialsException;
import br.com.latanks.hairremovalappointment_api.exceptions.impl.InvalidDateTimeException;
import br.com.latanks.hairremovalappointment_api.exceptions.impl.InvalidPhoneNumberException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;


@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    private ResponseEntity<Object> invalidCredentialsHandler(InvalidCredentialsException ex){
        ErrorResponse er = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getClass().getSimpleName(),
                List.of(ex.getMessage())
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityViolationHandle(DataIntegrityViolationException ex){
        ErrorResponse er = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getClass().getSimpleName(),
                List.of(ex.getLocalizedMessage())
                );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
    }

    @ExceptionHandler(InvalidDateTimeException.class)
    public ResponseEntity<Object> invalidDateTimeHandle(InvalidDateTimeException ex){
        ErrorResponse er = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getClass().getSimpleName(),
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<Object> invalidPhoneNumberHandler(InvalidPhoneNumberException ex){
        ErrorResponse er = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getClass().getSimpleName(),
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationHandle(ConstraintViolationException ex){
        ErrorResponse er = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getClass().getSimpleName(),
                ex.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .toList()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse er = new ErrorResponse(
                status.value(),
                ex.getClass().getSimpleName(),
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(status).body(er);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse er = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getClass().getSimpleName(),
                ex.getBindingResult().getFieldErrors().stream().map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                        .toList()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @Nullable
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorResponse er = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getClass().getSimpleName(),
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
}
