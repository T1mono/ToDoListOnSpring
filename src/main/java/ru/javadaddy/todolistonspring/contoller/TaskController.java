package ru.javadaddy.todolistonspring.contoller;

import lombok.RequiredArgsConstructor;
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
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.create(taskDto));
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> listAll() {
        return ResponseEntity.ok(taskService.listAll());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskDto>> getByStatus(
            @PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.filterByStatus(status));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getSortByStatus() {
        return ResponseEntity.ok(taskService.sortByStatus());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TaskDto> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<TaskDto> update(@PathVariable Long id,
                                          @RequestBody TaskDto taskDto

    ) {
        return ResponseEntity.ok(taskService.update(id, taskDto));
    }
}
