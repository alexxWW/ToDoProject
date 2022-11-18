package io.alex.todoproject.todoRepository;

import io.alex.todoproject.models.TodoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface TodoRepository extends CrudRepository<TodoEntity, UUID> {

    @Query("select * FROM TODO where rank=true")
    void deleteTodoByCompletedTrue();

    Optional<TodoEntity> findByUUID(UUID id);

    void deleteByUUID(UUID id);

    List<TodoEntity> findAll();

    int findTodoByMaxOrder();

}
