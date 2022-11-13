package io.alex.todoproject.controller;

import io.alex.todoproject.Todo;
import io.alex.todoproject.service.TodoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class TodoController {

    private TodoServiceImpl todoService;

    public TodoController(TodoServiceImpl todoService) {
        this.todoService = todoService;
    }

    @RequestMapping("todo/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Iterable<Todo>> getAllTodos() {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("todos/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Todo> createTodo(@Validated @RequestBody Todo todo) {
        return null;
    }

}
