package ru.javadaddy.todolistonspring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.javadaddy.todolistonspring.dto.TaskDto;
import ru.javadaddy.todolistonspring.enums.TaskStatus;
import ru.javadaddy.todolistonspring.exception.TaskNotFoundException;
import ru.javadaddy.todolistonspring.factory.TaskTestFactory;
import ru.javadaddy.todolistonspring.mapper.TaskMapper;
import ru.javadaddy.todolistonspring.model.Task;
import ru.javadaddy.todolistonspring.repostitory.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    //Создаем mock-репозиторий
    @Mock
    private TaskRepository taskRepository;

    //Создаем mock-маппер
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void sortByStatus_ShouldReturnSortedTasks() {
        // Arrange
        Task task1 = TaskTestFactory.createTask(1L, "Task 1", "Desc 1", null, TaskStatus.TODO);
        Task task2 = TaskTestFactory.createTask(2L, "Task 2", "Desc 2", null, TaskStatus.IN_PROGRESS);
        List<Task> tasks = List.of(task1, task2);

        when(taskRepository.findAll(Sort.by(Sort.Direction.ASC, "taskStatus")))
                .thenReturn(tasks);

        TaskDto dto1 = TaskTestFactory.createTaskDto(1L, "Task 1", "Desc 1", null, TaskStatus.TODO);
        TaskDto dto2 = TaskTestFactory.createTaskDto(2L, "Task 2", "Desc 2", null, TaskStatus.IN_PROGRESS);

        when(taskMapper.toDto(task1)).thenReturn(dto1);
        when(taskMapper.toDto(task2)).thenReturn(dto2);

        // Act
        List<TaskDto> result = taskService.sortByStatus();

        // Assert
        assertEquals(2, result.size());
        assertEquals(TaskStatus.TODO, result.get(0).getTaskStatus());
        assertEquals(TaskStatus.IN_PROGRESS, result.get(1).getTaskStatus());
        verify(taskRepository).findAll(Sort.by(Sort.Direction.ASC, "taskStatus"));
    }

    @Test
    void filterByStatus_ShouldReturnFilteredTasks() {
        // Arrange
        Task task = TaskTestFactory.createTask(1L, "Task 1", "Desc 1", null, TaskStatus.TODO);

        when(taskRepository.findByTaskStatus(TaskStatus.TODO))
                .thenReturn(List.of(task));

        TaskDto dto = TaskTestFactory.createTaskDto(1L, "Task", "Description", null, TaskStatus.TODO);

        when(taskMapper.toDto(task)).thenReturn(dto);

        // Act
        List<TaskDto> result = taskService.filterByStatus(TaskStatus.TODO);

        // Assert
        assertEquals(1, result.size());
        assertEquals(TaskStatus.TODO, result.get(0).getTaskStatus());
        verify(taskRepository).findByTaskStatus(TaskStatus.TODO);
    }

    @Test
    void update() {
        // Arrange
        Long taskId = 1L;
        LocalDate newDate = LocalDate.now().plusDays(5);


        TaskDto inputDto = TaskTestFactory.createTaskDto(null, "Updated Name", "Updated Desc", newDate, TaskStatus.IN_PROGRESS);
        Task existingTask = TaskTestFactory.createTask(taskId, "Old Name", "Old Desc", LocalDate.now(), TaskStatus.TODO);
        Task updatedTask = TaskTestFactory.createTask(taskId, "Updated Name", "Updated Desc", newDate, TaskStatus.IN_PROGRESS);
        TaskDto expectedDto = TaskTestFactory.createTaskDto(taskId, "Updated Name", "Updated Desc", newDate, TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(expectedDto);

        // Act
        TaskDto result = taskService.update(taskId, inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Updated Name", result.getName());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).toDto(updatedTask);
    }

    @Test
    void deleteById_ShouldDeleteTask() {
        // Arrange
        Long taskId = 1L;
        Task task = TaskTestFactory.createTask(taskId, "Task", "Description", null, TaskStatus.TODO);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        TaskDto expectedDto = TaskTestFactory.createTaskDto(taskId, "Task", "Description", null, TaskStatus.TODO);
        when(taskMapper.toDto(task)).thenReturn(expectedDto);

        // Act
        TaskDto result = taskService.deleteById(taskId);

        // Assert
        assertEquals(taskId, result.getId());
        verify(taskRepository).delete(task); // Изменили на delete(task)
    }

    @Test
    void create_ShouldCreateTask() {
        // Arrange
        TaskDto inputDto = TaskTestFactory.createTaskDto(null, "Test Task", "Test Description", null, TaskStatus.TODO);

        Task entity = TaskTestFactory.createTask(null, "Test Task", "Test Description", null, TaskStatus.TODO);
        Task savedEntity = TaskTestFactory.createTask(1L, "Test Task", "Test Description", null, TaskStatus.TODO);
        TaskDto expectedDto = TaskTestFactory.createTaskDto(1L, "Test Task", "Test Description", null, TaskStatus.TODO);

        when(taskMapper.toEntity(any(TaskDto.class))).thenReturn(entity);
        when(taskRepository.save(any(Task.class))).thenReturn(savedEntity);
        when(taskMapper.toDto(any(Task.class))).thenReturn(expectedDto);

        // Act
        TaskDto result = taskService.create(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Task", result.getName());

        // Проверяем, что методы были вызваны
        verify(taskMapper).toEntity(any(TaskDto.class));
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(any(Task.class));
    }

    @Test
    void listAll_ShouldReturnAllTasks() {
        // Arrange
        Task task1 = TaskTestFactory.createTask(1L, "Task 1", "Desc 1", null, TaskStatus.TODO);
        Task task2 = TaskTestFactory.createTask(2L, "Task 2", "Desc 2", null, TaskStatus.IN_PROGRESS);

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));

        TaskDto dto1 = TaskTestFactory.createTaskDto(1L, "Task 1", "Desc 1", null, TaskStatus.TODO);
        TaskDto dto2 = TaskTestFactory.createTaskDto(2L, "Task 2", "Desc 2", null, TaskStatus.IN_PROGRESS);


        when(taskMapper.toDto(task1)).thenReturn(dto1);
        when(taskMapper.toDto(task2)).thenReturn(dto2);

        // Act
        List<TaskDto> result = taskService.listAll();

        // Assert
        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void getById_ShouldReturnTask() {
        // Arrange
        Long taskId = 1L;
        Task task = TaskTestFactory.createTask(taskId, "Task", "Description", null, TaskStatus.TODO);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        TaskDto expectedDto = TaskTestFactory.createTaskDto(taskId, "Task", "Description", null, TaskStatus.TODO);

        when(taskMapper.toDto(task)).thenReturn(expectedDto);

        // Act
        TaskDto result = taskService.getById(taskId);

        // Assert
        assertEquals(taskId, result.getId());
        verify(taskRepository).findById(taskId);
    }

    @Test
    void getById_ShouldThrowWhenTaskNotFound() {
        // Arrange
        Long nonExistentId = 999L;

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getById(nonExistentId);
        });
    }
}