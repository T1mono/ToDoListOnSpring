package ru.javadaddy.todolistonspring.repostitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.javadaddy.todolistonspring.enums.TaskStatus;
import ru.javadaddy.todolistonspring.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //Сортировка задачи по статусу
    List<Task> findByTaskStatus(TaskStatus taskStatus);
}
