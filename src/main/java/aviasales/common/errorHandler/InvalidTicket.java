package aviasales.common.errorHandler;

public class InvalidTicket extends RuntimeException {
    public InvalidTicket(String message) {
        super(message);
    }
}
