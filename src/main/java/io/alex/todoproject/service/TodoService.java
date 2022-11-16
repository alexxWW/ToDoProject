package io.alex.todoproject.service;

import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.CreateTodoRequest;
import io.alex.todoproject.models.TodoResponse;
import io.alex.todoproject.models.TodoUpdateRequest;

import java.util.Optional;
import java.util.UUID;

public interface TodoService {

    Iterable<TodoResponse> getAll();

    Optional<TodoResponse> getTodoById(UUID id);

    Todo create(CreateTodoRequest todo);

    void deleteAllTodo();

    void deleteTodoById(UUID id);

    Todo updateTodoById(UUID id, TodoUpdateRequest todo);

    void deleteTodoByCompleted(boolean completed);

}
