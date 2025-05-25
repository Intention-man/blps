package aviasales.management.addition.validator_node.validation;

import java.io.Serializable;

public record ValidationResult(boolean isValid, String explanation) implements Serializable {
}
