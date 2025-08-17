package ru.javadaddy.todolistonspring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.javadaddy.todolistonspring.dto.TaskDto;
import ru.javadaddy.todolistonspring.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);

    void updateEntityFromDto(TaskDto taskDto, @MappingTarget Task task);
}
