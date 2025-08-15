package ru.javadaddy.todolistonspring.service;

import org.springframework.transaction.annotation.Transactional;
import ru.javadaddy.todolistonspring.dto.TaskDto;
import ru.javadaddy.todolistonspring.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    List<TaskDto> sortByStatus();

    List<TaskDto> filterByStatus(TaskStatus taskStatus);

    TaskDto update(Long id, TaskDto taskDto);

    TaskDto deleteById(Long id);

    @Transactional
    TaskDto create(TaskDto taskDto);

    List<TaskDto> listAll();

    TaskDto getById(Long id);
}
