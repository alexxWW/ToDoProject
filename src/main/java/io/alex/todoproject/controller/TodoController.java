package io.alex.todoproject.controller;

import io.alex.todoproject.exceptions.TodoConflictException;
import io.alex.todoproject.exceptions.TodoNotFoundException;
import io.alex.todoproject.models.*;
import io.alex.todoproject.service.TodoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
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
        return new ResponseEntity<>(convertRequestTodoTodoResponse(todoService.create(todoDto.getTitle()), uriBuilder), HttpStatus.CREATED);
    }

    private TodoResponse convertRequestTodoTodoResponse(Todo todo, UriComponentsBuilder uriBuilder) {
        String uri = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(8080).pathSegment("todos", "{id}").buildAndExpand(todo.getId()).toUriString();
        return TodoResponse.builder().id(todo.getId()).title(todo.getTitle()).completed(todo.isCompleted()).order(todo.getOrder()).url(uri).build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TodoResponse>> getAllTodos(UriComponentsBuilder uriBuilder) {
        return ResponseEntity.ok(convertListTodoToResponse(todoService.getAll(), uriBuilder));
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Optional<TodoResponse>> getTodoById(@PathVariable String id) throws TodoNotFoundException {
        Optional<Todo> getTodo = todoService.getTodoById(id);
        if(getTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertTodoToResponse(todoService.getTodoById(id)));
    }

    private Optional<TodoResponse> convertTodoToResponse(Optional<Todo> todo) {
        return Optional.ofNullable(TodoResponse.builder().id(todo.get().getId()).title(todo.get().getTitle()).completed(todo.get().isCompleted()).order(todo.get().getOrder()).url(todo.get().getUrl()).build());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTodoByCompleted(@RequestParam(value = "completed", required = false, defaultValue = "false") boolean isCompleted) {
        todoService.deleteTodoByCompleted(isCompleted);
        return ResponseEntity.noContent().build();

    }

    @PutMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TodoResponse> updateTodoById(@PathVariable String id, @Valid @RequestBody TodoUpdateRequest todo, UriComponentsBuilder uriBuilder) throws TodoNotFoundException, TodoConflictException {

        return ResponseEntity.ok(convertRequestTodoTodoResponse(todoService.updateById(id, todo), uriBuilder));
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteByUUID(@PathVariable String id) throws TodoNotFoundException {
        Optional<Todo> getTodo = todoService.getTodoById(id);
        if(getTodo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }

    private List<TodoResponse> convertListTodoToResponse(List<Todo> todo, UriComponentsBuilder uriBuilder) {
        List<TodoResponse> todosList = new ArrayList<>();
        for(Todo eachTodo : todo) {
            todosList.add(convertRequestTodoTodoResponse(eachTodo, uriBuilder));
        }
        return todosList;
    }
}
