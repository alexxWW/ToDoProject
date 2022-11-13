package io.alex.todoproject.service;

import io.alex.todoproject.Todo;

import java.util.Optional;

public interface TodoService {

    public Iterable<Todo> getAllTodo();

    public Optional<Todo> getTodoById(String id);

    Todo createTodo(Todo todo);

    public void deleteAllTodo();

    public void deleteTodoById(String id);

    public Todo updateTodoById(String id);

    public Todo updateTodo(Todo todoToUpdate);
}
