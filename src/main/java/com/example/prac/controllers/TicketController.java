package com.example.prac.controllers;

import com.example.prac.data.req.ComplexTravelSearchRequestDTO;
import com.example.prac.data.res.*;
import com.example.prac.data.req.SimpleTravelSearchRequestDTO;
import com.example.prac.service.TicketSearchService;
import com.example.prac.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {
    private TicketSearchService ticketSearchService;
    private TicketService ticketService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateTickets() {
        ticketService.generateAndSaveTickets();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/search_1_way_routes")
    public ResponseEntity<SearchResponseDTO> searchSimpleRoutes1Way(@RequestBody SimpleTravelSearchRequestDTO reqDto) {
        SearchResponseDTO response = ticketSearchService.searchSimpleRoutes(reqDto, false);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_round_trip_routes")
    public ResponseEntity<SearchResponseDTO> searchSimpleRoutesRoundTrip(@RequestBody SimpleTravelSearchRequestDTO reqDto) {
        SearchResponseDTO response = ticketSearchService.searchSimpleRoutes(reqDto, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_complex_routes")
    public ResponseEntity<SearchResponseDTO> searchComplexRoots(@RequestBody ComplexTravelSearchRequestDTO reqDto) {
        SearchResponseDTO response = ticketSearchService.searchComplexRoutes(reqDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketDTO>> getTickets() {
        return new ResponseEntity<>(ticketService.getAllTicketDTOs(), HttpStatus.OK);
    }
}