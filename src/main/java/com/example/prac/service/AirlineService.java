package com.example.prac.service;

import com.example.prac.data.model.Airline;
import com.example.prac.repository.AirlineRepository;

public class AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineService(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    public Airline findByName(String name) {
        return airlineRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Airline not found: " + name));
    }
}