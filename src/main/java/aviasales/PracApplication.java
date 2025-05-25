package aviasales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PracApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(PracApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PracApplication.class);
    }
}

// TODO задачи по расписанию:
//  1. Генерация рандомных билетов (API генерит, Validation валидирует)
//  2. Очистка старых билетов
