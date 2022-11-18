package io.alex.todoproject.service;

import io.alex.todoproject.exceptions.TodoConflictException;
import io.alex.todoproject.exceptions.TodoNotFoundException;
import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.TodoEntity;
import io.alex.todoproject.models.CreateTodoRequest;
import io.alex.todoproject.models.TodoUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface TodoService {

    List<Todo> getAll();

    Optional<Todo> getTodoById(UUID id) throws TodoNotFoundException;

    Todo create(String todo);

    void deleteAllTodo();

    void deleteTodoById(UUID id);

    Todo updateByUUID(UUID id, TodoUpdateRequest todo) throws TodoNotFoundException, TodoConflictException;

    void deleteTodoByCompleted(boolean completed);

}
