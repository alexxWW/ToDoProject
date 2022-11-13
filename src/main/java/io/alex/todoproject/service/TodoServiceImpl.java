package io.alex.todoproject.service;

import io.alex.todoproject.Todo;
import io.alex.todoproject.todoInterface.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoCrudService;

    public TodoServiceImpl(TodoRepository todoCrudService) {
         this.todoCrudService = todoCrudService;
    }

    @Override
    public Iterable<Todo> getAllTodo() {
        return todoCrudService.findAll();
    }

    @Override
    public Optional<Todo> getTodoById(String id) {
        return todoCrudService.findById(id);
    }

    @Override
    public Todo createTodo(Todo todo) {
        return null;
    }

    @Override
    public void deleteAllTodo() {
        todoCrudService.deleteAll();
    }

    @Override
    public void deleteTodoById(String id) {
        todoCrudService.deleteById(id);
    }

    @Override
    public Todo updateTodoById(String id) {
        return null;
    }

    @Override
    public Todo updateTodo(Todo todoToUpdate) {
        return null;
    }
}
