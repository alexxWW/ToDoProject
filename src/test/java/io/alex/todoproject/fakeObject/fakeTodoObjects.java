package io.alex.todoproject.fakeObject;

import io.alex.todoproject.models.*;

import java.util.List;
import java.util.UUID;

public class fakeTodoObjects {

    public static final CreateTodoRequest createTodoRequest = CreateTodoRequest.builder()
            .title("title")
            .build();

    public static final Todo todo = Todo.builder()
            .id(UUID.fromString("4811e9af-ce50-4a83-a6a4-ae316d38c536"))
            .title("title")
            .completed(false)
            .rank(1)
            .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .build();

    public static final TodoEntity todoEntity = TodoEntity.builder()
            .id(UUID.fromString("4811e9af-ce50-4a83-a6a4-ae316d38c536"))
            .title("title")
            .completed(false)
            .rank(1)
            .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .build();

    public static final TodoResponse todoResponse = TodoResponse.builder()
            .id(UUID.fromString("4811e9af-ce50-4a83-a6a4-ae316d38c536"))
            .title("title")
            .completed(false)
            .rank(1)
            .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .build();

    public static final TodoUpdateRequest todoUpdateRequest = TodoUpdateRequest.builder()
            .title("title")
            .completed(false)
            .rank(1)
            .build();

    public static final List<Todo> todoList = List.of(Todo.builder()
            .id(UUID.randomUUID())
            .title("cuisine")
            .rank(1)
            .completed(false)
            .url("www.google.fr")
            .build(),
            Todo.builder()
                    .id(UUID.randomUUID())
                    .title("sale de bain")
                    .rank(2).completed(false)
                    .url("www.google.fr")
                    .build());

    public static final TodoEntity todoEntityExpected = TodoEntity.builder()
            .id(UUID.fromString("4811e9af-ce50-4a83-a6a4-ae316d38c536"))
            .title("title")
            .completed(false)
            .rank(1)
            .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
            .build();

}
