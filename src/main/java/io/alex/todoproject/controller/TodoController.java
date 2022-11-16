package io.alex.todoproject.controller;

import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.CreateTodoRequest;
import io.alex.todoproject.models.TodoResponse;
import io.alex.todoproject.models.TodoUpdateRequest;
import io.alex.todoproject.service.TodoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "todos")
public class TodoController {

    private final TodoServiceImpl todoService;

    public TodoController(TodoServiceImpl todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody CreateTodoRequest todoDto) {
        return new ResponseEntity<>(todoService.create(todoDto), HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Iterable<TodoResponse>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAll());
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<TodoResponse>> getTodoById(@PathVariable UUID id) {
        Optional<TodoResponse> getTodo = todoService.getTodoById(id);
        if(getTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTodoByCompleted(@RequestParam(value = "completed", required = false) boolean isCompleted) {
        if (isCompleted) {
            todoService.deleteTodoByCompleted(isCompleted);
        } else {
            todoService.deleteAllTodo();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> updateTodoById(@PathVariable UUID id, @Valid @RequestBody TodoUpdateRequest todo) {
        Optional<TodoResponse> getTodo = todoService.getTodoById(id);
        Iterable<TodoResponse> getAllTodo = todoService.getAll();

        for(TodoResponse singleTodo : getAllTodo) {
            if(singleTodo.getRank() == todo.getRank()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        if(getTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        todoService.updateTodoById(id, todo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        Optional<TodoResponse> getTodo = todoService.getTodoById(id);
        if(getTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }
}
