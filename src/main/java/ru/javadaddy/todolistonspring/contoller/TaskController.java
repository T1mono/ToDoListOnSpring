package ru.javadaddy.todolistonspring.contoller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javadaddy.todolistonspring.dto.TaskDto;
import ru.javadaddy.todolistonspring.enums.TaskStatus;
import ru.javadaddy.todolistonspring.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("api/v1/spring-todo/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "API для управлением задачами")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Создать задачу")
    @ApiResponse(responseCode = "201", description = "Задача создана")
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody TaskDto taskDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(taskDto));
    }


    @GetMapping("{/id}")
    @Operation(summary = "Получить задачу по ID")
    @ApiResponse(responseCode = "200", description = "Задача найдена")
    @ApiResponse(responseCode = "404", description = "Не найдена")
    public ResponseEntity<TaskDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @GetMapping()
    @Operation(summary = "Все задачи")
    public ResponseEntity<List<TaskDto>> listAll() {
        return ResponseEntity.ok(taskService.listAll());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Фильтр по статусу")
    public ResponseEntity<List<TaskDto>> getByStatus(
            @Parameter(description = "Статус задачи") @PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.filterByStatus(status));
    }

    @GetMapping("/sorted-by-status")
    @Operation(summary = "Сортировка по статусу")
    public ResponseEntity<List<TaskDto>> getSortByStatus() {
        return ResponseEntity.ok(taskService.sortByStatus());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу")
    public ResponseEntity<TaskDto> deleteById(@Parameter(description = "ID задачи") @PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу")
    @ApiResponse(responseCode = "200", description = "Задача обновлена")
    @ApiResponse(responseCode = "404", description = "Не найдена")
    public ResponseEntity<TaskDto> update(@Parameter(description = "ID задачи") @PathVariable Long id,
                                          @Valid @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.update(id, taskDto));
    }
}
