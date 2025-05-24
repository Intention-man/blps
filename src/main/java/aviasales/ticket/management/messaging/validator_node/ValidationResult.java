package aviasales.ticket.management.messaging.validator_node;

import java.io.Serializable;

public record ValidationResult(boolean isValid, String explanation) implements Serializable {
}
