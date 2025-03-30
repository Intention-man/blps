package com.example.prac.controllers;

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

    @GetMapping("/1_way_search_routes")
    public ResponseEntity<TicketSearchResponseDTO> searchSimpleRoutes1Way(@RequestBody SimpleTravelSearchRequestDTO reqDto) {
        TicketSearchResponseDTO response = ticketSearchService.searchSimpleRoutes(reqDto, false);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/round_trip_search_routes")
    public ResponseEntity<TicketSearchResponseDTO> searchSimpleRoutesRoundTrip(@RequestBody SimpleTravelSearchRequestDTO reqDto) {
        TicketSearchResponseDTO response = ticketSearchService.searchSimpleRoutes(reqDto, true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketDTO>> getTickets() {
        return new ResponseEntity<>(ticketService.getAllTicketDTOs(), HttpStatus.OK);
    }
}