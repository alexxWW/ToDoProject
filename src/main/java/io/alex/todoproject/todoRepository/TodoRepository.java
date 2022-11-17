package io.alex.todoproject.todoRepository;

import io.alex.todoproject.models.TodoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TodoRepository extends CrudRepository<TodoEntity, UUID> {
    void deleteTodoByCompleted(boolean isCompleted);

    Optional<TodoEntity> findTodoByRank(int rank);

    Optional<TodoEntity> findByUUID(UUID id);

    void deleteByUUID(UUID id);

    List<TodoEntity> findAll();

    int findTodoByMaxOrder();


//    void deleteTodoByUUID(UUID uuid);
}
