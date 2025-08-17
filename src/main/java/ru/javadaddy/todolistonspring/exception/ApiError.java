package ru.javadaddy.todolistonspring.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
