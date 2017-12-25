package com.example.bartek.todoapp.database;

final public class UnexpectedDataException extends RuntimeException {
    public UnexpectedDataException(String message) {
        super(message);
    }
}
