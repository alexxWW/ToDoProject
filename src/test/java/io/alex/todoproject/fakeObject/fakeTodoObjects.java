package io.alex.todoproject.fakeObject;

import io.alex.todoproject.models.*;

import java.util.List;

public class fakeTodoObjects {

    public static final CreateTodoRequest createTodoRequest = CreateTodoRequest.builder()
            .title("title")
            .build();

    public static final Todo todo = Todo.builder()
            .id("4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .title("title")
            .completed(false)
            .order(1)
            .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .build();

    public static final TodoEntity todoEntity = TodoEntity.builder()
            .id("4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .title("title")
            .completed(false)
            .order(1)
            .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .build();

    public static final TodoResponse todoResponse = TodoResponse.builder()
            .id("4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .title("title")
            .completed(false)
            .order(1)
            .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .build();

    public static final TodoUpdateRequest todoUpdateRequest = TodoUpdateRequest.builder()
            .title("title")
            .completed(false)
            .order(1)
            .build();

    public static final List<Todo> todoList = List.of(Todo.builder()
            .id("4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .title("cuisine")
            .order(1)
            .completed(false)
            .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .build(),
            Todo.builder()
                    .id("4811e9af-ce50-4a83-a6a4-ae316d38c536")
                    .title("sale de bain")
                    .order(2)
                    .completed(false)
                    .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
                    .build());

    public static final List<TodoEntity> todoEntityList = List.of(TodoEntity.builder()
                    .id("4811e9af-ce50-4a83-a6a4-ae316d38c536")
                    .title("cuisine")
                    .order(1)
                    .completed(false)
                    .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
                    .build(),
            TodoEntity.builder()
                    .id("4811e9af-ce50-4a83-a6a4-ae316d38c536")
                    .title("sale de bain")
                    .order(2)
                    .completed(false)
                    .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
                    .build());

}
