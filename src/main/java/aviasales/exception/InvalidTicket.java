package aviasales.exception;

public class InvalidTicket extends RuntimeException {
    public InvalidTicket(String message) {
        super(message);
    }
}
