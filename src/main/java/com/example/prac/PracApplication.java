package com.example.prac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PracApplication {
    public static void main(String[] args) {
        SpringApplication.run(PracApplication.class, args);
    }
}

// TODO поиск билетов для простых маршрутов в обе стороны
// TODO поиск билетов для сложных маршрутов
// TODO пагинация
// TODO при выборе билета еще запрос, существует ли билет (просто getTicketById)
// TODO свои Exception-ы (как в модели)