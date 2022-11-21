package io.alex.todoproject.todoRepository;

import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.TodoEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface TodoRepository extends CrudRepository<TodoEntity, UUID> {

    @Modifying
    @Query("delete FROM TODO t where t.completed=true")
    void deleteTodoByCompletedTrue();

    Optional<TodoEntity> findById(String id);

    void deleteById(String id);

    List<TodoEntity> findAll();

    TodoEntity findTopByOrderByOrderDesc();

}
