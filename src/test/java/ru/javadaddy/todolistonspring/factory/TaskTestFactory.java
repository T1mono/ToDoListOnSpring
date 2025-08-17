package ru.javadaddy.todolistonspring.factory;

import ru.javadaddy.todolistonspring.dto.TaskDto;
import ru.javadaddy.todolistonspring.enums.TaskStatus;
import ru.javadaddy.todolistonspring.model.Task;

import java.time.LocalDate;

public class TaskTestFactory {
    public static Task createTask(
            Long id,
            String name,
            String description,
            LocalDate periodOfExecution,
            TaskStatus taskStatus
    ) {
        Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setDescription(description);
        task.setPeriodOfExecution(periodOfExecution);
        task.setTaskStatus(taskStatus);
        return task;
    }

    public static TaskDto createTaskDto(
            Long id,
            String name,
            String description,
            LocalDate periodOfExecution,
            TaskStatus taskStatus

    ) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(id);
        taskDto.setName(name);
        taskDto.setDescription(description);
        taskDto.setPeriodOfExecution(periodOfExecution);
        taskDto.setTaskStatus(taskStatus);
        return taskDto;
    }
}
