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
import ru.javadaddy.todolistonspring.mapper.TaskMapper;
import ru.javadaddy.todolistonspring.model.Task;
import ru.javadaddy.todolistonspring.repostitory.TaskRepository;

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
        Task task1 = new Task();
        task1.setTaskStatus(TaskStatus.TODO);
        Task task2 = new Task();
        task2.setTaskStatus(TaskStatus.IN_PROGRESS);

        List<Task> tasks = List.of(task1, task2);
        when(taskRepository.findAll(Sort.by(Sort.Direction.ASC, "taskStatus")))
                .thenReturn(tasks);

        TaskDto dto1 = new TaskDto();
        dto1.setTaskStatus(TaskStatus.TODO);
        TaskDto dto2 = new TaskDto();
        dto2.setTaskStatus(TaskStatus.IN_PROGRESS);

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
        Task task = new Task();
        task.setTaskStatus(TaskStatus.TODO);

        when(taskRepository.findByTaskStatus(TaskStatus.TODO))
                .thenReturn(List.of(task));

        TaskDto dto = new TaskDto();
        dto.setTaskStatus(TaskStatus.TODO);

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

        TaskDto inputDto = new TaskDto();
        inputDto.setName("Updated Name");
        inputDto.setDescription("Updated Description");
        inputDto.setTaskStatus(TaskStatus.IN_PROGRESS);

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setName("Old Name");
        existingTask.setDescription("Old Description");
        existingTask.setTaskStatus(TaskStatus.TODO);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setName(inputDto.getName());
        updatedTask.setDescription(inputDto.getDescription());
        updatedTask.setTaskStatus(inputDto.getTaskStatus());

        TaskDto expectedDto = new TaskDto();
        expectedDto.setId(taskId);
        expectedDto.setName(updatedTask.getName());
        expectedDto.setDescription(updatedTask.getDescription());
        expectedDto.setTaskStatus(updatedTask.getTaskStatus());

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
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        TaskDto expectedDto = new TaskDto();
        expectedDto.setId(taskId);

        when(taskMapper.toDto(task)).thenReturn(expectedDto);

        // Act
        TaskDto result = taskService.deleteById(taskId);

        // Assert
        assertEquals(taskId, result.getId());
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void create_ShouldCreateTask() {
        // Arrange
        TaskDto inputDto = new TaskDto();
        inputDto.setName("Test Task");
        inputDto.setDescription("Test Description");
        inputDto.setTaskStatus(TaskStatus.TODO);

        Task entity = new Task();
        entity.setName(inputDto.getName());
        entity.setDescription(inputDto.getDescription());
        entity.setTaskStatus(inputDto.getTaskStatus());

        Task savedEntity = new Task();
        savedEntity.setId(1L);
        savedEntity.setName(entity.getName());
        savedEntity.setDescription(entity.getDescription());
        savedEntity.setTaskStatus(entity.getTaskStatus());

        TaskDto expectedDto = new TaskDto();
        expectedDto.setId(1L);
        expectedDto.setName(savedEntity.getName());
        expectedDto.setDescription(savedEntity.getDescription());
        expectedDto.setTaskStatus(savedEntity.getTaskStatus());

        // Используем any() для более гибкого сравнения
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
        Task task1 = new Task();
        Task task2 = new Task();

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));

        TaskDto dto1 = new TaskDto();
        TaskDto dto2 = new TaskDto();

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
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        TaskDto expectedDto = new TaskDto();
        expectedDto.setId(taskId);

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
        when(taskRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getById(nonExistentId);
        });
    }
}