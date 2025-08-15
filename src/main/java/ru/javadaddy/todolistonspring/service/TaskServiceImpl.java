package ru.javadaddy.todolistonspring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.MappingException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javadaddy.todolistonspring.dto.TaskDto;
import ru.javadaddy.todolistonspring.enums.TaskStatus;
import ru.javadaddy.todolistonspring.exception.TaskNotFoundException;
import ru.javadaddy.todolistonspring.mapper.TaskMapper;
import ru.javadaddy.todolistonspring.model.Task;
import ru.javadaddy.todolistonspring.repostitory.TaskRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Сортировка задачи по статусу.
     * @return Возращает отсортированный список задач по статусу.
     */
    @Override
    public List<TaskDto> sortByStatus() {
        List<Task> foundTasks = taskRepository.findAll(Sort.by(Sort.Direction.ASC, "taskStatus"));
        return foundTasks.stream()
                .map(task -> taskMapper.toDto(task))
                .collect(Collectors.toList());
    }


    /**
     * Получение списка задач по статусу.
     *
     * @param taskStatus
     * @return Возвращает отфильтрованный список задач по статусу.
     */
    @Override
    public List<TaskDto> filterByStatus(TaskStatus taskStatus) {
        log.debug("Get tasks by status: {}", taskStatus);
        List<Task> foundTasks = taskRepository.findByTaskStatus(taskStatus);
        return foundTasks.stream()
                .map(task -> taskMapper.toDto(task))
                .collect(Collectors.toList());
    }

    /**
     * Обновление задачи по ID.
     *
     * @param id
     * @param taskDto
     * @return Возвращает обновлунную задачу.
     */
    @Override
    @Transactional
    public TaskDto update(Long id, TaskDto taskDto) {
// Находим существующую задачу
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Запись с ID \"" + id + "\" не существует"));

        log.debug("Updating task with id: {}", id);

        // Обновляем поля существующей задачи
        existingTask.setName(taskDto.getName());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setPeriodOfExecution(taskDto.getPeriodOfExecution());
        existingTask.setTaskStatus(taskDto.getTaskStatus());

        // Сохраняем обновлённую задачу (merge произойдёт автоматически)
        Task updatedTask = taskRepository.save(existingTask);
        log.debug("Task updated successfully with id: {}", updatedTask.getId());

        return taskMapper.toDto(updatedTask);
    }

    /**
     * Удаление задачи.
     *
     * @param id
     * @return Взвращает удаленный объект.
     */
    @Override
    @Transactional
    public TaskDto deleteById(Long id) {
        //Поиск сущности
        final Optional<Task> foundById = taskRepository.findById(id);
        log.debug("Task start by deleted record by id: {}", id);
        if (foundById.isPresent()) {
            taskRepository.deleteById(id);
            log.debug("Task end by deleted record by id: {}", id);
            return taskMapper.toDto(foundById.get());
        } else {
            return null;
        }
    }

    /**
     * Создание задачи.
     *
     * @param taskDto
     * @return Возвращает объект созданной задачи.
     */
    @Override
    @Transactional
    public TaskDto create(TaskDto taskDto) {
        if (taskDto == null) {
            throw new IllegalArgumentException("DTO задачи не может быть null");
        }

        // Запрещаем передачу ID при создании
//        if (taskDto.getId() != null) {
//            throw new IllegalArgumentException("Нельзя указывать ID при создании новой задачи");
//        }

        log.debug("Task start create record: {}", taskDto);

        Task task = taskMapper.toEntity(taskDto);
        if (task == null) {
            throw new MappingException("Ошибка преобразования DTO в сущность");
        }

        final Task saved = taskRepository.save(task);
        return taskMapper.toDto(saved);
    }

    /**
     * Получить список задач.
     *
     * @return Возвращает весь список задач.
     */
    @Override
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
     *
     * @param id
     * @return Возращает задачу найденную по ID.
     */
    @Override
    public TaskDto getById(Long id) {
        log.debug("Task get by id: {}", id);
        final Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача с ID" + id + " не найдена"));
        log.debug("Task record received {}", task);
        return taskMapper.toDto(task);
    }
}
