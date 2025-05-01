package com.example.prac.errorHandler;

public class IncorrectAdminCode extends RuntimeException {
    public IncorrectAdminCode() {
        super("Некорректный админ код");
    }
    public IncorrectAdminCode(String message) {
        super(message);
    }
}
