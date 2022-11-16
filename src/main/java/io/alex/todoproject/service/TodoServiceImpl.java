package io.alex.todoproject.service;

import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.CreateTodoRequest;
import io.alex.todoproject.models.TodoResponse;
import io.alex.todoproject.models.TodoUpdateRequest;
import io.alex.todoproject.todoRepository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoCrudService;

    public TodoServiceImpl(TodoRepository todoCrudService) {
         this.todoCrudService = todoCrudService;
    }


    @Override
    public Iterable<TodoResponse> getAll() {
        return todoResponsesListRequest(todoCrudService.findAll());
    }

    @Override
    public Optional<TodoResponse> getTodoById(UUID id) {
        return Optional.ofNullable(getTodoResponse(todoCrudService.findByUUID(id)));
    }

    @Override
    public Todo create(CreateTodoRequest todo) {
        return todoCrudService.save(todoCreateRequest(todo));
    }

    @Override
    public void deleteAllTodo() {
        todoCrudService.deleteAll();
    }

    @Override
    public void deleteTodoById(UUID id) {
        todoCrudService.deleteByUUID(id);
    }

    @Override
    public Todo updateTodoById(UUID id, TodoUpdateRequest todo) {
        Optional<TodoResponse> todoToUpdate = this.getTodoById(id);

        if(todoToUpdate.isEmpty()) {
            return null;
        }
        TodoUpdateRequest updatedTodo = TodoUpdateRequest.builder().title(todo.getTitle()).completed(todo.isCompleted()).rank(todo.getRank()).build();
        return todoCrudService.updateByUUID(id, updatedTodo);
    }

    @Override
    public void deleteTodoByCompleted(boolean isCompleted) {
        todoCrudService.deleteTodoByCompleted(isCompleted);
    }

    private Todo todoCreateRequest(CreateTodoRequest todo) {
        if(todo == null) {
            return null;
        }
        return Todo.builder().title(todo.getTitle()).rank(1).completed(false).url("www.google.fr").build();
    }

    private TodoResponse todoResponseRequest(Todo todo) {
        return TodoResponse.builder().id(todo.getId()).title(todo.getTitle()).rank(todo.getRank()).completed(todo.isCompleted()).url(todo.getUrl()).build();
    }
    private Iterable<TodoResponse> todoResponsesListRequest(Iterable<Todo> todos) {
        List<TodoResponse> todoResponses = new ArrayList<>();

        for(Todo todo : todos) {
            todoResponses.add(todoResponseRequest(todo));
        }
        return todoResponses;
    }

    private TodoResponse getTodoResponse(Optional<Todo> todo) {
        return TodoResponse.builder().id(todo.get().getId()).title(todo.get().getTitle()).completed(todo.get().isCompleted()).rank(todo.get().getRank()).url(todo.get().getUrl()).build();
    }
}
