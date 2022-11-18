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
        verify(todoRepository, times(1)).findTodoByMaxOrder();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should create a new todo with a non existing rank")
    void createTodoWithNonExistingRank() {

        when(todoRepository.save(any(TodoEntity.class))).thenReturn(todoEntity);
        when(todoRepository.findTodoByMaxOrder()).thenReturn(2);
        String title = "title";

        Todo actual = todoService.create(title);

        Todo expected = Todo.builder()
                .id(todoEntity.getId())
                .title("title")
                .completed(false)
                .url(todoEntity.getUrl())
                .order(3)
                .build();

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
        verify(todoRepository, times(1)).findTodoByMaxOrder();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should get todo by id")
    void getById() throws TodoNotFoundException {
        when(todoRepository.findByUUID(todoEntity.getId())).thenReturn(Optional.of(todoEntity));

        todoService.getTodoById(todo.getId());
        Todo expectedTodo = Todo.builder()
                .id(todoEntity.getId())
                .title(todoEntity.getTitle())
                .completed(todoEntity.isCompleted())
                .order(todoEntity.getOrder())
                .url(todoEntity.getUrl())
                .build();

        assertThat(todo).usingRecursiveComparison().isEqualTo(expectedTodo);
        verify(todoRepository, times(1)).findByUUID(todoEntity.getId());
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
                        .url("www.google.fr")
                        .build(),
                Todo.builder()
                        .id(todoList.get(0).getId())
                        .title("sale de bain")
                        .order(2).completed(false)
                        .url("www.google.fr")
                        .build());
        assertThat(todoList).usingRecursiveComparison().isEqualTo(expectedTodosList);
        verify(todoRepository).findAll();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should can't find todo")
    void cantGetById() {

        when(todoRepository.findByUUID(todoEntity.getId())).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(todo.getId()));
        verify(todoRepository, times(1)).findByUUID(todoEntity.getId());
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should delete todo by id")
    void deleteById() {

        doNothing().when(todoRepository).deleteByUUID(todoList.get(0).getId());
        todoService.deleteTodoById(todoList.get(0).getId());

        verify(todoRepository, times(1)).deleteByUUID(eq(todoList.get(0).getId()));
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

        when(todoRepository.findByUUID(todo.getId())).thenReturn(Optional.of(todoEntity));
        when(todoRepository.save(todoEntityToSave)).thenReturn(todoEntity);

        todoService.getTodoById(todo.getId());
        todoService.updateByUUID(todoEntityToSave.getId(), todoUpdateRequest);

        assertThat(todoEntity).usingRecursiveComparison().isEqualTo(todoExpected);

        verify(todoRepository, times(2)).findByUUID(todo.getId());
        verify(todoRepository).save(todoEntityToSave);
        verify(todoRepository).findAll();
        verify(todoRepository).findTodoByMaxOrder();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should can't update todo because of not found")
    void cantUpdate() {
        UUID uuid = UUID.randomUUID();

        when(todoRepository.findByUUID(uuid)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.updateByUUID(uuid, todoUpdateRequest));
        verify(todoRepository).findByUUID(uuid);
        verifyNoMoreInteractions(todoRepository);
    }

}
