package com.example.prac.service;

import com.example.prac.data.model.Airline;
import com.example.prac.data.model.City;
import com.example.prac.data.model.Ticket;
import com.example.prac.repository.AirlineRepository;
import com.example.prac.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class TicketService {
    private TicketRepository ticketRepository;
    private CityService cityService;
    private AirlineRepository airlineRepository;

    public List<Ticket> generateAndSaveRandomTickets(int count) {
        List<Ticket> tickets = new ArrayList<>();
        Random random = new Random();

        List<City> cities = cityService.getAllCities();
        if (cities.isEmpty()) {
            System.out.println("No cities found in the database");
            return new ArrayList<>();
        }

        List<Airline> airlines = airlineRepository.findAll();
        if (airlines.isEmpty()) {
            System.out.println("No airlines found in the database");
            return new ArrayList<>();
        }

        for (int i = 0; i < count; i++) {
            Airline airline = airlines.get(random.nextInt(airlines.size()));

            City departureCity = cities.get(random.nextInt(cities.size()));
            City arrivalCity = cities.get(random.nextInt(cities.size()));

            while (departureCity.equals(arrivalCity)) {
                arrivalCity = cities.get(random.nextInt(cities.size()));
            }

            LocalDateTime departureTime = LocalDateTime.now().plusDays(random.nextInt(30)).plusHours(random.nextInt(24));
            LocalDateTime arrivalTime = departureTime.plusHours(random.nextInt(10) + 1);


            Ticket ticket = Ticket.builder()
                    .airline(airline)
                    .flightNumber(String.format("%s%03d", airline.getName().substring(0, 2).toUpperCase(), random.nextInt(999) + 1))
                    .departureCity(departureCity)
                    .departureDatetime(departureTime)
                    .arrivalCity(arrivalCity)
                    .arrivalDatetime(arrivalTime)
                    .price(random.nextInt() * 500 + 100)
                    .build();
            tickets.add(ticket);
        }

        return ticketRepository.saveAll(tickets);
    }

    public List<Ticket> findAvailableTickets(String departureCity, String destinationCity, LocalDateTime startTime, LocalDateTime endTime) {
        return ticketRepository.findByDepartureCityAndArrivalCityAndDepartureTimeBetween(departureCity, destinationCity, startTime, endTime);
    }

    public List<Ticket> findAvailableTicketsAfter(String departureCity, String destinationCity, LocalDateTime startTime) {
        return ticketRepository.findByDepartureCityAndArrivalCityAndDepartureTimeAfter(departureCity, destinationCity, startTime);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}