package aviasales.ticket.search.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import aviasales.ticket.search.data.dto.ComplexTravelSearchRequestDTO;
import aviasales.ticket.search.data.dto.SimpleTravelSearchRequestDTO;
import aviasales.ticket.search.data.dto.SearchResponseDTO;
import aviasales.common.data.dto.TicketDTO;
import aviasales.ticket.search.service.DateTimeService;
import aviasales.ticket.search.service.TicketSearchService;
import aviasales.common.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/search")
@AllArgsConstructor
public class SearchTicketController {
    private final DateTimeService dateTimeService;
    private final TicketSearchService ticketSearchService;
    private final TicketService ticketService;

    @GetMapping("/search_1_way_routes")
    public ResponseEntity<Object> searchSimpleRoutes1Way(
            @Valid @RequestBody SimpleTravelSearchRequestDTO reqDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        if (page < 0 || limit < 1) {
            return new ResponseEntity<>("page should by >= 0 and limit should be > 0", HttpStatus.BAD_REQUEST);
        }
        dateTimeService.validate(reqDto);
        SearchResponseDTO response = ticketSearchService.searchSimpleRoutes(reqDto, false, page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_round_trip_routes")
    public ResponseEntity<Object> searchSimpleRoutesRoundTrip(
            @Valid @RequestBody SimpleTravelSearchRequestDTO reqDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        if (page < 0 || limit < 1) {
            return new ResponseEntity<>("page should by >= 0 and limit should be > 0", HttpStatus.BAD_REQUEST);
        }
        dateTimeService.validate(reqDto);
        SearchResponseDTO response = ticketSearchService.searchSimpleRoutes(reqDto, true, page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_complex_routes")
    public ResponseEntity<Object> searchComplexRoots(
            @Valid @RequestBody ComplexTravelSearchRequestDTO reqDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        if (page < 0 || limit < 1) {
            return new ResponseEntity<>("page should by >= 0 and limit should be > 0", HttpStatus.BAD_REQUEST);
        }
        dateTimeService.validate(reqDto);

        SearchResponseDTO response = ticketSearchService.searchComplexRoutes(reqDto, page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketDTO>> getTickets(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int limit) {
        return new ResponseEntity<>(ticketService.getAllTicketDTOs(page, limit), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<TicketDTO> getTicketById(@RequestParam Long id) {
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}