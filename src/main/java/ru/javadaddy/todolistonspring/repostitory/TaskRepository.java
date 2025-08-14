package ru.javadaddy.todolistonspring.repostitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javadaddy.todolistonspring.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
