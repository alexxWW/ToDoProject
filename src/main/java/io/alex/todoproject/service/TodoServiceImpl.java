package io.alex.todoproject.service;

import io.alex.todoproject.exceptions.TodoNotFoundException;
import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.CreateTodoRequest;
import io.alex.todoproject.models.TodoResponse;
import io.alex.todoproject.models.TodoUpdateRequest;
import io.alex.todoproject.todoRepository.TodoRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoCrudService;

    public TodoServiceImpl(TodoRepository todoCrudService) {
         this.todoCrudService = todoCrudService;
    }


    @Override
    public Iterable<Todo> getAll() {
        return todoCrudService.findAll();
    }

    @Override
    public Optional<Todo> getTodoById(UUID id) {
        return todoCrudService.findByUUID(id);
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
    public Todo updateByUUID(UUID id, TodoUpdateRequest todo) throws TodoNotFoundException {
        Optional<Todo> getTodo = getTodoById(id);

        if(getTodo.isEmpty()) {
            throw new TodoNotFoundException();
        }
        Todo updatedTodo = Todo.builder().id(id).title(todo.getTitle()).completed(todo.isCompleted()).rank(todo.getRank()).build();
        return todoCrudService.save(updatedTodo);
    }

    @Override
    public void deleteTodoByCompleted(boolean isCompleted) {
        todoCrudService.deleteTodoByCompleted(isCompleted);
    }

    private Todo todoCreateRequest(CreateTodoRequest todo) {

        if(todo == null) {
            return null;
        }
        return Todo.builder().title(todo.getTitle()).rank(1).completed(false).build();
    }

}
