package io.alex.todoproject.controller;

import io.alex.todoproject.exceptions.TodoNotFoundException;
import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.CreateTodoRequest;
import io.alex.todoproject.models.TodoResponse;
import io.alex.todoproject.models.TodoUpdateRequest;
import io.alex.todoproject.service.TodoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody CreateTodoRequest todoDto, UriComponentsBuilder uriBuilder) {
        return new ResponseEntity<>(convertRequestTodoTodoResponse(todoService.create(todoDto), uriBuilder), HttpStatus.CREATED);
    }

    private TodoResponse convertRequestTodoTodoResponse(Todo todo, UriComponentsBuilder uriBuilder) {
        String uri = uriBuilder.pathSegment("todos", "{id}").buildAndExpand(todo.getId()).toUriString();
        return TodoResponse.builder().id(todo.getId()).title(todo.getTitle()).completed(todo.isCompleted()).rank(todo.getRank()).url(uri).build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TodoResponse>> getAllTodos() {
        return ResponseEntity.ok(convertListTodoToResponse(todoService.getAll()));
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<TodoResponse>> getTodoById(@PathVariable UUID id) {
        Optional<Todo> getTodo = todoService.getTodoById(id);
        if(getTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertTodoToResponse(todoService.getTodoById(id)));
    }

    private Optional<TodoResponse> convertTodoToResponse(Optional<Todo> todo) {
        return Optional.ofNullable(TodoResponse.builder().id(todo.get().getId()).title(todo.get().getTitle()).completed(todo.get().isCompleted()).rank(todo.get().getRank()).url(todo.get().getUrl()).build());
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
    public ResponseEntity<TodoResponse> updateTodoById(@PathVariable UUID id, @Valid @RequestBody TodoUpdateRequest todo) throws TodoNotFoundException {
        Iterable<Todo> listTodo = todoService.getAll();

        for(Todo singleTodo : listTodo) {
            if(singleTodo.getRank() == todo.getRank()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        return ResponseEntity.ok(convertTodoToResponse(todoService.updateByUUID(id, todo)));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteByUUID(@PathVariable UUID id) {
        Optional<Todo> getTodo = todoService.getTodoById(id);
        if(getTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }

    private TodoResponse convertTodoToResponse(Todo todo) {
        return TodoResponse.builder().id(todo.getId()).title(todo.getTitle()).completed(todo.isCompleted()).rank(todo.getRank()).url(todo.getUrl()).build();
    }

    private List<TodoResponse> convertListTodoToResponse(Iterable<Todo> todo) {
        List<TodoResponse> todosList = new ArrayList<>();
        for(Todo eachTodo : todo) {
            todosList.add(convertTodoToResponse(eachTodo));
        }
        return todosList;
    }
}
