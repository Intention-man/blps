package com.example.prac.service;

import com.example.prac.data.model.Airline;
import com.example.prac.errorHandler.AirlineNotFoundException;
import com.example.prac.repository.AirlineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public Airline findByName(String name) {
        return airlineRepository.findByName(name)
                .orElseThrow(() -> new AirlineNotFoundException("Авиакомпания не найдена: " + name));
    }
}