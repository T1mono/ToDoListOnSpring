package ru.javadaddy.todolistonspring.exception;

public class TaskNotFoundException extends RuntimeException {

    //Конструктор с собщением об ошибке
    public TaskNotFoundException(String message) {
        super(message);
    }
}
