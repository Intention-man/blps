package com.example.prac.service.data;

import com.example.prac.model.dataEntity.City;
import com.example.prac.model.dataEntity.Ticket;
import com.example.prac.repository.data.CityRepository;
import com.example.prac.repository.data.TicketRepository;
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
    private CityRepository cityRepository;

    public List<Ticket> generateAndSaveRandomTickets(int count) {
        List<Ticket> tickets = new ArrayList<>();
        Random random = new Random();

        List<City> cities = cityRepository.findAll();
        if (cities.isEmpty()) {
            System.out.println("No cities found in the database.  Add some cities first!");
            return new ArrayList<>();
        }

        String[] airlines = {"United", "Delta", "British Airways", "Air France", "Japan Airlines", "Qantas", "Emirates"};

        for (int i = 0; i < count; i++) {
            City departureCity = cities.get(random.nextInt(cities.size()));
            City arrivalCity = cities.get(random.nextInt(cities.size()));

            while (departureCity.equals(arrivalCity)) {
                arrivalCity = cities.get(random.nextInt(cities.size()));
            }

            LocalDateTime departureTime = LocalDateTime.now().plusDays(random.nextInt(30)).plusHours(random.nextInt(24)); // Up to 30 days in the future
            LocalDateTime arrivalTime = departureTime.plusHours(random.nextInt(10) + 1); // Flight duration up to 10 hours

            Ticket ticket = Ticket.builder()
                    .airline(airlines[random.nextInt(airlines.length)])
                    .flightNumber(String.format("%s%03d", airlines[random.nextInt(airlines.length)].substring(0, 2).toUpperCase(), random.nextInt(999) + 1)) // Example: UA123
                    .departureCity(departureCity.getName())
                    .departureTime(departureTime)
                    .arrivalCity(arrivalCity.getName())
                    .arrivalTime(arrivalTime)
                    .price(random.nextDouble() * 500 + 100)
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

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}