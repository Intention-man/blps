package aviasales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PracApplication {
    public static void main(String[] args) {
        SpringApplication.run(PracApplication.class, args);
    }
}

// TODO не пускать дальше при ошибке регистрации/авторизации
// TODO обработка ошибок