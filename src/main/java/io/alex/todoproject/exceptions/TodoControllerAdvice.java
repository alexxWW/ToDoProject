package io.alex.todoproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TodoControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler({TodoNotFoundException.class, TodoConflictException.class})
    public ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) throws Exception {

        if(ex instanceof TodoNotFoundException) {
            return handleTodoNotFoundException();
        } else if(ex instanceof TodoConflictException) {
            return handleTodoConflictException();
        }
        throw ex;
    }

    private ResponseEntity<Object> handleTodoNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private ResponseEntity<Object> handleTodoConflictException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
