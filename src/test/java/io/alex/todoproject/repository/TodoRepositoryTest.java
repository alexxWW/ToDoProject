package io.alex.todoproject.repository;

import io.alex.todoproject.models.TodoEntity;
import io.alex.todoproject.todoRepository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static io.alex.todoproject.fakeObject.fakeTodoObjects.todoEntityList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void findAll() {

        when(todoRepository.findAll()).thenReturn(todoEntityList);
        List<TodoEntity> expected = todoRepository.findAll();

        assertThat(expected.size()).usingRecursiveComparison().isEqualTo(todoEntityList);
    }
}