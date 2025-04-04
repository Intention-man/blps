package com.example.prac.errorHandler;

public class TravelDateTimeException extends RuntimeException {
    public TravelDateTimeException() {
        super("Время возвращения не может быть раньше времени отправления.");
    }

    public TravelDateTimeException(String message) {
        super(message);
    }
}