package io.alex.todoproject.repository;

import io.alex.todoproject.models.TodoEntity;
import io.alex.todoproject.todoRepository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
public class TodoRepositoryTest {

    @MockBean
    private TodoRepository todoRepository;

    @Test
    void deleteTodoByCompletedTrue() {
        TodoEntity entityToSave = TodoEntity.builder().title("title").completed(true).build();


        when(todoRepository.save(entityToSave)).thenReturn(entityToSave);
        doNothing().when(todoRepository).deleteTodoByCompletedTrue();

        List<TodoEntity> expectedTodoEntity = List.of();

        assertThat(todoRepository.findAll()).usingRecursiveComparison().isEqualTo(expectedTodoEntity);

        verify(todoRepository).findAll();
        verifyNoMoreInteractions(todoRepository);

    }

//    @Test
//    void findTopByOrderByOrderDesc() {
//        String id = UUID.randomUUID();
//        TodoEntity entityByOrder = TodoEntity.builder().id(id).title("title").order(1).completed(true).url().build();
//        TodoEntity entityByOrder1 = TodoEntity.builder().title("title").order(2).completed(true).build();
//
//        when(todoRepository.save(any(TodoEntity.class))).thenReturn(entityByOrder);
//
//        todoRepository.findTopByOrderByOrderDesc();
//
//        assertThat(todoRepository.findTopByOrderByOrderDesc()).usingRecursiveComparison().isEqualTo(entityByOrder);
//    }
}