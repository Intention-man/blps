package aviasales.ticket.management.controller;

import aviasales.ticket.management.messaging.dto.TicketAdditionResultMessage;
import aviasales.ticket.management.messaging.validator_node.TicketAdditionValidateNodeService;
import aviasales.ticket.management.messaging.validator_node.TicketReqStatusDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/editor-validation")
@AllArgsConstructor
public class ValidationNodeController {
    private final TicketAdditionValidateNodeService validationService;

    @GetMapping("/manual-validation/{jobId}")
    public ResponseEntity<List<TicketReqStatusDTO>> getPendingRequests(@PathVariable String jobId) {
        List<TicketReqStatusDTO> requests = validationService.manuallyValidation(jobId);
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/handle-result")
    public ResponseEntity<Void> saveAutoCheckResult(@RequestBody TicketAdditionResultMessage resultMessage) {
        validationService.sendManualValidationResult(resultMessage);
        return ResponseEntity.accepted().build();
    }
}