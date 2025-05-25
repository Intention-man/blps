package aviasales.management.addition.api_node.http;

import aviasales.management.addition.api_node.TicketAdditionApiNodeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets/editor-api")
@AllArgsConstructor
public class AdditionTicketController {
    private final TicketAdditionApiNodeService ticketAdditionApiNodeService;

    @PostMapping("/add-async")
    public ResponseEntity<AsyncJobResponse> addTicketsAsyncAuto(@Valid @RequestBody TicketAdditionReqDTO req) {
        if (req.getTickets().isEmpty()) {
            return ResponseEntity.badRequest().body(new AsyncJobResponse(null, "EMPTY_REQUEST", "Ticket list cannot be empty"));
        }
        String jobId = ticketAdditionApiNodeService.storeAndSend(req);
        return ResponseEntity.accepted().body(new AsyncJobResponse(jobId, "PROCESSING", null));
    }
}