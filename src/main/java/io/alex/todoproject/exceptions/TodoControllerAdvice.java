package io.alex.todoproject.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TodoControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) throws Exception {

        if(ex instanceof TodoNotFoundException) {
            return handleTodoNotFoundException();
        }
        throw ex;
    }

    private ResponseEntity<Object> handleTodoNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
