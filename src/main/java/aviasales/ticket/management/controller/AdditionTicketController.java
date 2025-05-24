package aviasales.ticket.management.controller;

import aviasales.common.data.dto.TicketDTO;
import aviasales.common.service.TicketService;
import aviasales.ticket.management.data.dto.AsyncJobResponse;
import aviasales.ticket.management.data.dto.TicketAdditionReqDTO;
import aviasales.ticket.management.messaging.api_node.TicketAdditionApiNodeService;
import aviasales.ticket.management.messaging.validator_node.TicketAdditionValidateNodeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tickets/editor-api")
@AllArgsConstructor
public class AdditionTicketController {
    private final TicketService ticketService;
    private final TicketAdditionApiNodeService ticketAdditionApiNodeService;
    private final TicketAdditionValidateNodeService validationService;

    @PostMapping("/add-async")
    public ResponseEntity<AsyncJobResponse> addTicketsAsyncAuto(@Valid @RequestBody TicketAdditionReqDTO req) {
        if (req.getTickets().isEmpty()) {
            return ResponseEntity.badRequest().body(new AsyncJobResponse(null, "EMPTY_REQUEST", "Ticket list cannot be empty"));
        }
        String jobId = ticketAdditionApiNodeService.storeAndSend(req);
        return ResponseEntity.accepted().body(new AsyncJobResponse(jobId, "PROCESSING", null));
    }


    @PostMapping("/add")
    public ResponseEntity<String> addTickets(@Valid @RequestBody List<TicketDTO> ticketsDto) {
        ticketService.addTickets(ticketsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateTickets(@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        ticketService.generateAndSaveTickets(date);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}