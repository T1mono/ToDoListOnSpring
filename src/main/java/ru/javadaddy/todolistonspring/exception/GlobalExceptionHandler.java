//package ru.javadaddy.todolistonspring.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(TaskNotFoundException.class)
//    public ResponseEntity<ApiError> handleTaskNotFound(TaskNotFoundException ex) {
//        ApiError error = new ApiError(
//                ex.getMessage(),
//                HttpStatus.NOT_FOUND.value(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
//        ApiError error = new ApiError(
//                ex.getMessage(),
//                HttpStatus.BAD_REQUEST.value(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//}
