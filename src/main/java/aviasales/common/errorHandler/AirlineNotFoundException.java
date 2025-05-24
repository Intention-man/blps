package aviasales.common.errorHandler;

public class AirlineNotFoundException extends RuntimeException {
    public AirlineNotFoundException() {
        super("Авиакомпания не найдена.");
    }

    public AirlineNotFoundException(String message) {
        super(message);
    }
}