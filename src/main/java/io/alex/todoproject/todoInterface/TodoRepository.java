package io.alex.todoproject.todoInterface;

import io.alex.todoproject.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface TodoRepository extends CrudRepository<Todo, String> {

}
