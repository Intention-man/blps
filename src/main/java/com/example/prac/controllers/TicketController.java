package com.example.prac.controllers;

import com.example.prac.DTO.data.CityDTO;
import com.example.prac.DTO.data.ComplexRouteSearchRequestDTO;
import com.example.prac.DTO.data.TicketSearchResponseDTO;
import com.example.prac.mappers.CityMapper;
import com.example.prac.model.dataEntity.City;
import com.example.prac.service.data.TicketSearchService;
import com.example.prac.service.data.TicketService;
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
    private CityMapper cityMapper;

    @PostMapping("/search")
    public ResponseEntity<TicketSearchResponseDTO> searchTickets(@RequestBody ComplexRouteSearchRequestDTO searchRequestDTO) {
        TicketSearchResponseDTO results = ticketSearchService.searchTickets(searchRequestDTO);
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
        ticketService.getAllCities().add(city);
        return new ResponseEntity<>("City created", HttpStatus.CREATED);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityDTO>> getAllCities() {
        List<CityDTO> cities = ticketService.getAllCities().stream().map(
                city -> cityMapper.mapTo(city)
        ).collect(Collectors.toList());
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}