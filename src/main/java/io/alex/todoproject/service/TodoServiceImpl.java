package io.alex.todoproject.service;

import io.alex.todoproject.exceptions.TodoConflictException;
import io.alex.todoproject.exceptions.TodoNotFoundException;
import io.alex.todoproject.models.*;
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
    public List<Todo> getAll() {
        return convertListTodoEntityToTodo(todoCrudService.findAll());
    }

    @Override
    public Optional<Todo> getTodoById(UUID id) throws TodoNotFoundException {

        return convertTodoEntityToTodo(Optional.of(todoCrudService.findByUUID(id).orElseThrow(TodoNotFoundException::new)));
    }

    @Override
    public Todo create(String todo) {
        int maxRank = todoCrudService.findTodoByMaxOrder();
        TodoEntity todoToEntity = TodoEntity.builder().title(todo).rank(maxRank + 1).build();
        return convertEntityToTodo(todoCrudService.save(todoToEntity));
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
    public Todo updateByUUID(UUID id, TodoUpdateRequest todo) throws TodoNotFoundException, TodoConflictException {
        Optional<Todo> getTodo = getTodoById(id);
        List<Todo> allTodos = getAll();

        if(getTodo.isEmpty()) {
            throw new TodoNotFoundException();
        }
        for(Todo singleTodo : allTodos) {
            if(singleTodo.getRank() == todo.getRank()) {
                throw new TodoConflictException();
            }
        }
        TodoEntity updatedTodo = TodoEntity.builder().id(id).title(todo.getTitle()).completed(todo.isCompleted()).rank(todo.getRank()).build();
        return convertEntityToTodo(todoCrudService.save(updatedTodo));
    }

    @Override
    public void deleteTodoByCompleted(boolean isCompleted) {
        todoCrudService.deleteTodoByCompleted(isCompleted);
    }

    private Todo convertTodoEntity(TodoEntity todo) {
        return Todo.builder().id(todo.getId()).title(todo.getTitle()).completed(todo.isCompleted()).rank(todo.getRank()).url(todo.getUrl()).build();
    }

    private List<Todo> convertListTodoEntityToTodo(List<TodoEntity> todo) {
        List<Todo> todosList = new ArrayList<>();
        for(TodoEntity eachTodo : todo) {
            todosList.add(convertTodoEntity(eachTodo));
        }
        return todosList;
    }

    private Optional<Todo> convertTodoEntityToTodo(Optional<TodoEntity> todo) {
        return Optional.ofNullable(Todo.builder().id(todo.get().getId()).title(todo.get().getTitle()).completed(todo.get().isCompleted()).rank(todo.get().getRank()).url(todo.get().getUrl()).build());
    }

    private Todo convertEntityToTodo(TodoEntity todo) {
        return Todo.builder().id(todo.getId()).title(todo.getTitle()).completed(todo.isCompleted()).rank(todo.getRank()).url(todo.getUrl()).build();
    }

}
