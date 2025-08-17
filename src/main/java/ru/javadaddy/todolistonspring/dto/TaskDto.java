package ru.javadaddy.todolistonspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.javadaddy.todolistonspring.enums.TaskStatus;

import java.time.LocalDate;

@Data
public class TaskDto {
    @JsonIgnore
    private Long id;

    @NotBlank(message = "Название задачи не может быть пустым")
    @Size(min = 1, max = 100, message = "Название задачи должно быть от 1 до 100 символов")
    private String name;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    @FutureOrPresent(message = "Дата должна быть в настоящем или будущем")
    private LocalDate periodOfExecution;

    @NotNull(message = "Статус задачи обязателен")
    private TaskStatus taskStatus;
}
