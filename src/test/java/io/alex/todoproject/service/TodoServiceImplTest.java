package io.alex.todoproject.service;

import io.alex.todoproject.exceptions.TodoConflictException;
import io.alex.todoproject.exceptions.TodoNotFoundException;
import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.TodoEntity;
import io.alex.todoproject.todoRepository.TodoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.alex.todoproject.fakeObject.fakeTodoObjects.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Todo Service test")
@ExtendWith(MockitoExtension.class)
public class TodoServiceImplTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;

    @Test
    @DisplayName("Should create a new todo with his title")
    void createTodo() {

        when(todoRepository.findAll()).thenReturn(List.of());
        when(todoRepository.save(any(TodoEntity.class))).thenReturn(todoEntity);
        String title = "title";

        Todo actual = todoService.create(title);

        Todo expected = Todo.builder()
                .id(todoEntity.getId())
                .title("title")
                .completed(false)
                .url(todoEntity.getUrl())
                .order(1)
                .build();


        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
        verify(todoRepository).findAll();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should create a new todo with a non existing rank")
    void createTodoWithNonExistingRank() {
        String title = "title";
        when(todoRepository.save(any(TodoEntity.class))).thenReturn(todoEntity);
        when(todoRepository.findAll()).thenReturn(List.of(todoEntity));
        when(todoRepository.findTopByOrderByOrderDesc()).thenReturn(todoEntity);

        Todo actual = todoService.create(title);
        Todo expected = Todo.builder()
                .id(actual.getId())
                .title(actual.getTitle())
                .completed(actual.isCompleted())
                .url(actual.getUrl())
                .order(actual.getOrder())
                .build();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
        verify(todoRepository, times(1)).findTopByOrderByOrderDesc();
        verify(todoRepository).findAll();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should get todo by id")
    void getById() throws TodoNotFoundException {
        when(todoRepository.findById(todoEntity.getId())).thenReturn(Optional.of(todoEntity));

        todoService.getTodoById(todo.getId());
        Todo expectedTodo = Todo.builder()
                .id(todoEntity.getId())
                .title(todoEntity.getTitle())
                .completed(todoEntity.isCompleted())
                .order(todoEntity.getOrder())
                .url(todoEntity.getUrl())
                .build();

        assertThat(todo).usingRecursiveComparison().isEqualTo(expectedTodo);
        verify(todoRepository, times(1)).findById(todoEntity.getId());
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should get all todos")
    void getAll() {
        when(todoRepository.findAll()).thenReturn(todoEntityList);

        todoService.getAll();
        List<Todo> expectedTodosList = List.of(Todo.builder().id(todoList.get(0).getId())
                        .title("cuisine")
                        .order(1)
                        .completed(false)
                        .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
                        .build(),
                Todo.builder()
                        .id(todoList.get(0).getId())
                        .title("sale de bain")
                        .order(2).completed(false)
                        .url("http://localhost:8080/todos/4811e9af-ce50-4a83-a6a4-ae316d38c536")
                        .build());
        assertThat(todoList).usingRecursiveComparison().isEqualTo(expectedTodosList);
        verify(todoRepository).findAll();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should can't find todo")
    void cantGetById() {

        when(todoRepository.findById(todoEntity.getId())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(todo.getId()));
        verify(todoRepository, times(1)).findById(todoEntity.getId());
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should delete todo by id")
    void deleteById() {

        doNothing().when(todoRepository).deleteById(todoList.get(0).getId());
        todoService.deleteTodoById(todoList.get(0).getId());

        verify(todoRepository, times(1)).deleteById(eq(todoList.get(0).getId()));
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should delete all todos")
    void deleteAll() {

        doNothing().when(todoRepository).deleteAll();
        todoService.deleteAllTodo();
        assertThat(todoService.getAll()).isEmpty();

        verify(todoRepository).deleteAll();
        verify(todoRepository).findAll();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should update todo")
    void updateTodo() throws TodoConflictException, TodoNotFoundException {

        Todo todoExpected = Todo.builder()
                .id(todoEntity.getId())
                .title("title")
                .completed(false)
                .order(1)
                .url(todoEntity.getUrl())
                .build();

        TodoEntity todoEntityToSave = TodoEntity.builder()
                .id(todoEntity.getId())
                .title(todoEntity.getTitle())
                .completed(todoEntity.isCompleted())
                .order(todoEntity.getOrder())
                .build();

        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todoEntity));
        when(todoRepository.save(todoEntityToSave)).thenReturn(todoEntity);

        todoService.getTodoById(todo.getId());
        todoService.updateById(todoEntityToSave.getId(), todoUpdateRequest);

        assertThat(todoEntity).usingRecursiveComparison().isEqualTo(todoExpected);

        verify(todoRepository, times(2)).findById(todo.getId());
        verify(todoRepository).save(todoEntityToSave);
        verify(todoRepository).findAll();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should can't update todo because of not found")
    void cantUpdate() {
        String uuid = String.valueOf(UUID.randomUUID());

        when(todoRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.updateById(uuid, todoUpdateRequest));
        verify(todoRepository).findById(uuid);
        verifyNoMoreInteractions(todoRepository);
    }

}
