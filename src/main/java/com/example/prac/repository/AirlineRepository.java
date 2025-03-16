package com.example.prac.repository;

import com.example.prac.data.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Airline findByName(String name);
}