package com.example.prac.controllers;

import com.example.prac.data.res.CityDTO;
import com.example.prac.data.res.TicketDTO;
import com.example.prac.data.req.simple.SimpleTravelSearchRequestDTO;
import com.example.prac.data.res.TicketSearchResponse;
import com.example.prac.data.model.City;
import com.example.prac.mappers.CityMapper;
import com.example.prac.service.CityService;
import com.example.prac.service.TicketSearchService;
import com.example.prac.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {
    private TicketSearchService ticketSearchService;
    private TicketService ticketService;
    private CityService cityService;
    private CityMapper cityMapper;

    @PostMapping("/generate")
    public ResponseEntity<String> generateTickets() {
        ticketService.generateAndSaveTickets();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketDTO>> getTickets() {
        return new ResponseEntity<>(ticketService.getAllTicketDTOs(), HttpStatus.OK);
    }

    @GetMapping("/search_simple")
    public ResponseEntity<TicketSearchResponse> searchSimpleRoutes(@RequestBody SimpleTravelSearchRequestDTO simpleTravelSearchRequestDTO) {
        TicketSearchResponse results = ticketSearchService.searchSimpleRoutes(simpleTravelSearchRequestDTO);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/cities")
    public ResponseEntity<String> createCity(@RequestBody CityDTO cityDTO) {
        City city = City.builder()
                .name(cityDTO.getName())
                .build();
        cityService.getAllCities().add(city);
        return new ResponseEntity<>("City created", HttpStatus.CREATED);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityDTO>> getAllCities() {
        List<CityDTO> cities = cityService.getAllCities().stream().map(
                city -> cityMapper.mapTo(city)
        ).collect(Collectors.toList());
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}