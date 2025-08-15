package ru.javadaddy.todolistonspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.javadaddy.todolistonspring.enums.TaskStatus;

import java.time.LocalDate;

@Data
public class TaskDto {
    @JsonIgnore
    private Long id;
    private String name;
    private String description;
    private LocalDate periodOfExecution;
    private TaskStatus taskStatus;
}
