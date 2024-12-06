package org.codiz.onshop.ControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.ControllerAdvice.custom.*;
import org.codiz.onshop.dtos.response.UserGeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException");
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error -> {
            String filedName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(filedName, errorMessage);
        }));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach((violation) -> {
            String filedName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();

            errors.put(filedName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<UserGeneralResponse> handleAccessDeniedException(AccessDeniedException ex){
        log.warn("AccessDeniedException occurred.{}", ex.getMessage());

        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setHttpStatus(HttpStatus.FORBIDDEN);
        userGeneralResponse.setMessage("Permission Denied.");
        userGeneralResponse.setDate(new Date());

        return new ResponseEntity<>(userGeneralResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<UserGeneralResponse> handleAuthenticationEntryPointException(AuthenticationException ex){
        log.warn("Authentication Exception.");

        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setHttpStatus(HttpStatus.FORBIDDEN);
        userGeneralResponse.setMessage("Authentication failed. "+ex.getMessage());
        userGeneralResponse.setDate(new Date());

        return new ResponseEntity<>(userGeneralResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<UserGeneralResponse> handleNoResourceFoundException(NoResourceFoundException ex){
        log.warn("request path not found.");

        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setMessage(ex.getMessage());
        userGeneralResponse.setDate(new Date());
        userGeneralResponse.setHttpStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(userGeneralResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<UserGeneralResponse> handleUserAlreadyExistException(UserAlreadyExistException ex){
        log.warn("UserAlreadyExistException");
        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setMessage(ex.getMessage());
        userGeneralResponse.setDate(new Date());
        userGeneralResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userGeneralResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<UserGeneralResponse> handleExpiredJwtException(ExpiredJwtException ex){
        log.warn("ExpiredJwtException");

        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setMessage(ex.getMessage());
        userGeneralResponse.setDate(new Date());
        userGeneralResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(userGeneralResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<UserGeneralResponse> handleInvalidEmailFormatException(InvalidEmailFormatException ex){
        log.warn("InvalidEmailFormatException");

        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        userGeneralResponse.setDate(new Date());
        userGeneralResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(userGeneralResponse, HttpStatus.BAD_REQUEST);
    }

/*    @ExceptionHandler(UserDoesNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleUserDoesNotExistException(UserDoesNotExistException ex){
        Map<String, String> errors = new HashMap<>();

        String message = ex.getMessage();
        errors.put("message", message);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }*/

/*
    @ExceptionHandler(UsersCreationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleUsersCreationFailedException(UsersCreationFailedException ex){
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMessage();
        Throwable cause = ex.getCause();
        errors.put("message"+ message+"cause", cause.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
*/

    @ExceptionHandler(InvalidTokensException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<UserGeneralResponse> handleInvalidTokensException(InvalidTokensException ex){
        log.warn("Invalid token passed.");

        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setMessage("Something went wrong.");
        userGeneralResponse.setDate(new Date());
        userGeneralResponse.setHttpStatus(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(userGeneralResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(EntityDeletionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleEntityDeletionException(EntityDeletionException ex){
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMessage();
        errors.put("message"+ message+"cause", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String,String>> handleResourceNotFoundException(ResourceNotFoundException ex){
        Map<String,String> errors = new HashMap<>();
        String message = ex.getMessage();
        errors.put("message",message);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceCreationFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String,String>> handleResourceCreationFailedException(ResourceCreationFailedException ex){
        Map<String,String> errors = new HashMap<>();
        String message = ex.getMessage();
        errors.put("message",message);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventsCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleEventsCreationException
            (EventsCreationException ex){
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMessage();
        errors.put("message"+ message+"cause", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<UserGeneralResponse> handleEventNotFoundException(EventNotFoundException e){
        log.warn("EventNotFoundException");

        UserGeneralResponse userGeneralResponse = new UserGeneralResponse();
        userGeneralResponse.setMessage(e.getMessage());
        userGeneralResponse.setDate(new Date());
        userGeneralResponse.setHttpStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(userGeneralResponse, HttpStatus.NOT_FOUND);
    }

}
