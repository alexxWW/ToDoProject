package io.alex.todoproject.todoRepository;

import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.TodoResponse;
import io.alex.todoproject.models.TodoUpdateRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TodoRepository extends CrudRepository<Todo, UUID> {
    void deleteTodoByCompleted(boolean isCompleted);

    Optional<Todo> findTodoByRank(int rank);

    Optional<Todo> findByUUID(UUID id);

    void deleteByUUID(UUID id);

    Todo updateByUUID(UUID id, TodoUpdateRequest todo);

//    void deleteTodoByUUID(UUID uuid);
}
