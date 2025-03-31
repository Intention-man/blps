package com.example.prac.controllers;

import com.example.prac.data.req.ComplexTravelSearchRequestDTO;
import com.example.prac.data.res.*;
import com.example.prac.data.req.SimpleTravelSearchRequestDTO;
import com.example.prac.service.TicketSearchService;
import com.example.prac.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {
    private TicketSearchService ticketSearchService;
    private TicketService ticketService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateTickets(@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        ticketService.generateAndSaveTickets(date);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/search_1_way_routes")
    public ResponseEntity<Object> searchSimpleRoutes1Way(
            @RequestBody SimpleTravelSearchRequestDTO reqDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        if (page < 0 || limit < 1){
            return new ResponseEntity<>("page should by >= 0 and limit should be > 0", HttpStatus.BAD_REQUEST);
        }
        SearchResponseDTO response = ticketSearchService.searchSimpleRoutes(reqDto, false, page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_round_trip_routes")
    public ResponseEntity<Object> searchSimpleRoutesRoundTrip(
            @RequestBody SimpleTravelSearchRequestDTO reqDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        if (page < 0 || limit < 1){
            return new ResponseEntity<>("page should by >= 0 and limit should be > 0", HttpStatus.BAD_REQUEST);
        }
        SearchResponseDTO response = ticketSearchService.searchSimpleRoutes(reqDto, true, page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search_complex_routes")
    public ResponseEntity<Object> searchComplexRoots(
            @RequestBody ComplexTravelSearchRequestDTO reqDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        if (page < 0 || limit < 1){
            return new ResponseEntity<>("page should by >= 0 and limit should be > 0", HttpStatus.BAD_REQUEST);
        }
        SearchResponseDTO response = ticketSearchService.searchComplexRoutes(reqDto, page, limit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketDTO>> getTickets() {
        return new ResponseEntity<>(ticketService.getAllTicketDTOs(), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<TicketDTO> getTicketById(@RequestParam Long id) {
        return ticketService.getTicketById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}