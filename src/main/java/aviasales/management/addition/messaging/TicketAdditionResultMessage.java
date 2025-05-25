package aviasales.management.addition.messaging;

import java.io.Serializable;
import java.util.List;

public record TicketAdditionResultMessage(
        String jobId,
        List<TicketValidationResultItem> validationResults
) implements Serializable {
}