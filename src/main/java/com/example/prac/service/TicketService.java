package com.example.prac.service;

import com.example.prac.data.res.TicketDTO;
import com.example.prac.data.model.Airline;
import com.example.prac.data.model.City;
import com.example.prac.data.model.ServiceClass;
import com.example.prac.data.model.Ticket;
import com.example.prac.mappers.TicketMapper;
import com.example.prac.repository.AirlineRepository;
import com.example.prac.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {
    private TicketRepository ticketRepository;
    private CityService cityService;
    private AirlineRepository airlineRepository;
    private TicketMapper ticketMapper;

    public void generateAndSaveTickets() {
        List<Ticket> tickets = new ArrayList<>();
        Random random = new Random();

        List<City> cities = cityService.getAllCities();
        if (cities.isEmpty()) {
            System.out.println("No cities found in the database");
            return;
        }

        List<Airline> airlines = airlineRepository.findAll();
        if (airlines.isEmpty()) {
            System.out.println("No airlines found in the database");
            return;
        }

        for (Airline airline : airlines) {
            for (City departureCity : cities) {
                for (City arrivalCity : cities) {
                    if (!departureCity.equals(arrivalCity)) {
                        ServiceClass serviceClass = ServiceClass.values()[random.nextInt(ServiceClass.values().length)];
                        String flightNumber = String.format("%s%03d", airline.getName().substring(0, 2).toUpperCase(), random.nextInt(900) + 100);
                        LocalDate departureDate = LocalDate.now().plusDays(random.nextInt(30));
                        LocalTime departureTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
                        LocalDate arrivalDate = departureDate.plusDays(random.nextInt(1));
                        LocalTime arrivalTime = departureTime.plusHours(random.nextInt(10) + 1);
                        int price = random.nextInt(400) + 100;
                        int availableSeats = random.nextInt(3) + 1;

                        Ticket ticket = Ticket.builder()
                                .airline(airline)
                                .serviceClass(serviceClass)
                                .flightNumber(flightNumber)
                                .departureCity(departureCity)
                                .departureDate(departureDate)
                                .departureTime(departureTime)
                                .arrivalCity(arrivalCity)
                                .arrivalDate(arrivalDate)
                                .arrivalTime(arrivalTime)
                                .price(price)
                                .availableSeats(availableSeats)
                                .build();
                        tickets.add(ticket);
                    }
                }
            }
        }
        ticketRepository.saveAll(tickets);
    }

    public List<TicketDTO> getAllTicketDTOs() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticketMapper::mapTo)
                .collect(Collectors.toList());
    }

    public double calculateTravelTimeInHours(Ticket ticket) {
        LocalDateTime departureDateTime = LocalDateTime.of(ticket.getDepartureDate(), ticket.getDepartureTime());
        LocalDateTime arrivalDateTime = LocalDateTime.of(ticket.getArrivalDate(), ticket.getArrivalTime());

        long minutes = java.time.Duration.between(departureDateTime, arrivalDateTime).toMinutes();
        return minutes / 60.0;
    }
}