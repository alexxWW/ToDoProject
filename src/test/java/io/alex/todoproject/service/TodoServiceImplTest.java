package io.alex.todoproject.service;

import io.alex.todoproject.exceptions.TodoNotFoundException;
import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.TodoEntity;
import io.alex.todoproject.todoRepository.TodoRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static io.alex.todoproject.fakeObject.fakeTodoObjects.*;
import static java.util.UUID.fromString;
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

        todoService.create("title");
        Todo actual = Todo.builder().id(todoEntity.getId())
                .title(todoEntity.getTitle())
                .completed(todoEntity.isCompleted())
                .rank(todoEntity.getRank())
                .url(todoEntity.getUrl())
                .build();
        Todo expected = Todo.builder()
                .id(todoEntity.getId())
                .title("title")
                .completed(false)
                .rank(1)
                .url(todoEntity.getUrl())
                .build();

        assertEquals(actual, expected, "Object are not equals");

        verify(todoRepository, times(1)).save(any(TodoEntity.class));
        verify(todoRepository, times(1)).findTodoByMaxOrder();
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    @DisplayName("Should delete all todos")
    void deleteAll() {

        doNothing().when(todoRepository).deleteAll();
        todoService.deleteAllTodo();
        assertThat(todoService.getAll()).isEmpty();
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
                .rank(todoEntity.getRank())
                .url(todoEntity.getUrl())
                .build();

        assertEquals(todo, expectedTodo);
        verify(todoRepository, times(1)).findByUUID(todoEntity.getId());
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
}
