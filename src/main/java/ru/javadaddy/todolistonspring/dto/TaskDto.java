package ru.javadaddy.todolistonspring.dto;

import lombok.Data;
import ru.javadaddy.todolistonspring.enums.TaskStatus;

import java.time.LocalDate;

@Data
public class TaskDto {
    private String name;
    private String description;
    private LocalDate periodOfExecution;
    private TaskStatus taskStatus;
}
