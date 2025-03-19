package com.example.prac.controllers;

import com.example.prac.data.DTO.CityDTO;
import com.example.prac.data.DTO.complex.req.ComplexRouteSearchRequest;
import com.example.prac.data.DTO.simple.req.SimpleRouteSearchRequestDTO;
import com.example.prac.data.DTO.simple.res.TicketSearchResponse;
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

    @GetMapping("/search_simple")
    public ResponseEntity<TicketSearchResponse> searchSimpleRoutes(@RequestBody SimpleRouteSearchRequestDTO simpleRouteSearchRequestDTO) {
        TicketSearchResponse results = ticketSearchService.searchSimpleRoutes(simpleRouteSearchRequestDTO);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/search_simple")
    public ResponseEntity<TicketSearchResponse> searchComplexRoutes(@RequestBody ComplexRouteSearchRequest complexRouteSearchRequest) {
        TicketSearchResponse results = ticketSearchService.searchComplexRoutes(complexRouteSearchRequest);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateTickets(@RequestParam(defaultValue = "100") int count) {
        ticketService.generateAndSaveRandomTickets(count);
        return new ResponseEntity<>("Generated " + count + " tickets.", HttpStatus.CREATED);
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