package ru.javadaddy.todolistonspring.enums;

public enum TaskStatus {
    TODO("К выполнению"),

    IN_PROGRESS("В процессе"),

    DONE("Завершено");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
