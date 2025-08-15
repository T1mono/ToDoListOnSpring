package ru.javadaddy.todolistonspring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MappingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javadaddy.todolistonspring.dto.TaskDto;
import ru.javadaddy.todolistonspring.exception.TaskNotFoundException;
import ru.javadaddy.todolistonspring.mapper.TaskMapper;
import ru.javadaddy.todolistonspring.model.Task;
import ru.javadaddy.todolistonspring.repostitory.TaskRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Создание задачи.
     * @param taskDto
     * @return
     */
    @Transactional
    public TaskDto create(TaskDto taskDto) {
        //Проверка входных данных
        if (taskDto == null) {
            throw new IllegalArgumentException("DTO задачи не может быть null");
        }

        final Long taskDtoId = taskDto.getId();

        //Проверка на существование ID
        if (taskDtoId != null && taskRepository.existsById(taskDtoId)) {
            log.error("Record task with id: {} exist", taskDtoId);
            throw new RuntimeException("Запись с ID \"" + taskDtoId + "\" уже существует");
        }
        log.debug("Task start create record: {}", taskDto);

        //Маппинг
        Task task = taskMapper.toEntity(taskDto);

        if (task == null) {
            throw new MappingException("Ошибка преобразования DTO в сущность");
        }

        final Task saved = taskRepository.save(task);

        return taskMapper.toDto(saved);
    }

    /**
     * Получить список задач.
     * @return
     */
    public List<TaskDto> listAll() {
        final List<Task> foundResult = taskRepository.findAll();
        if (foundResult.isEmpty()) {
            log.warn("No tasks found in database");
            return Collections.emptyList();
        }
        return foundResult.stream()
                .map(task -> taskMapper.toDto(task))
                .collect(Collectors.toList());
    }

    /**
     * Получить задачу по ID.
     * @param id
     * @return
     */
    public TaskDto getById(Long id) {
        log.debug("Task get by id: {}", id);
        final Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача с ID" + id + " не найдена"));
        log.debug("Task record received {}", task);
        return taskMapper.toDto(task);
    }
}
