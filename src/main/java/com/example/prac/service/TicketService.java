package com.example.prac.service;

import com.example.prac.data.model.Airline;
import com.example.prac.data.model.City;
import com.example.prac.data.model.ServiceClass;
import com.example.prac.data.model.Ticket;
import com.example.prac.data.res.TicketDTO;
import com.example.prac.errorHandler.CityNotFoundException;
import com.example.prac.errorHandler.InvalidTicket;
import com.example.prac.mappers.TicketMapper;
import com.example.prac.repository.AirlineRepository;
import com.example.prac.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {
    private TicketRepository ticketRepository;
    private CityService cityService;
    private AirlineRepository airlineRepository;
    private TicketMapper ticketMapper;

    @Transactional
    public void addTickets(List<TicketDTO> ticketsDTO) {
        List<Ticket> newTickets = new ArrayList<>();

        int i = 0;
        for (TicketDTO ticketDTO : ticketsDTO) {
            try {
                cityService.findByName(ticketDTO.getDepartureCity());
            } catch (CityNotFoundException e) {
                cityService.save(ticketDTO.getDepartureCity());
            }
            try {
                cityService.findByName(ticketDTO.getArrivalCity());
            } catch (CityNotFoundException e) {
                cityService.save(ticketDTO.getArrivalCity());
            }

            if (!isValid(ticketDTO)) {
                throw new InvalidTicket("Невалидный билет №" + i);
            }

            newTickets.add(ticketMapper.mapFrom(ticketDTO));
            i++;
        }

        ticketRepository.saveAll(newTickets);
    }

    public void generateAndSaveTickets(LocalDate departureDate) {
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
                List<City> otherCities = new ArrayList<>(cities);
                otherCities.remove(departureCity);
                for (City arrivalCity : otherCities) {
                    ServiceClass serviceClass = ServiceClass.values()[random.nextInt(ServiceClass.values().length)];
                    String flightNumber = String.format("%s%03d", airline.getName().substring(0, 2).toUpperCase(), random.nextInt(900) + 100);

//                    LocalDate departureDate = LocalDate.now().plusDays(random.nextInt(2));
//                    LocalDate departureDate = LocalDate.of(2025, 3, 28).plusDays(random.nextInt(2));


                    LocalTime departureTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
                    LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);

                    LocalDateTime arrivalDateTime = departureDateTime
                            .plusHours(1 + random.nextInt(10))
                            .plusMinutes(random.nextInt(60));
                    LocalDate arrivalDate = arrivalDateTime.toLocalDate();
                    LocalTime arrivalTime = arrivalDateTime.toLocalTime();

                    int price = random.nextInt(400) + 100;
                    int availableSeats = random.nextInt(3) + 1;

                    Ticket ticket = Ticket.builder()
                            .airline(airline)
                            .serviceClass(serviceClass)
                            .flightNumber(flightNumber)
                            .departureCity(departureCity)
                            .departureDate(departureDate)
                            .departureTime(departureTime)
                            .departureDateTime(departureDateTime)
                            .arrivalCity(arrivalCity)
                            .arrivalDate(arrivalDate)
                            .arrivalTime(arrivalTime)
                            .arrivalDateTime(arrivalDateTime)
                            .price(price)
                            .availableSeats(availableSeats)
                            .build();
                    ticket.setHours(calculateTravelTimeInHours(ticket));

                    tickets.add(ticket);
                }
            }
        }
        ticketRepository.saveAll(tickets);
    }

    public List<TicketDTO> getAllTicketDTOs(int page, int limit) {
        List<Ticket> tickets = ticketRepository.findAll();
        return sublistByPageAndLimit(tickets, page, limit)
                .stream()
                .map(ticketMapper::mapTo)
                .collect(Collectors.toList());
    }

    public Optional<TicketDTO> getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(ticketMapper::mapTo);
    }

    public <T> List<T> sublistByPageAndLimit(List<T> list, int page, int limit) {
        int toIndex = Math.max(Math.min((page + 1) * limit, list.size()) - 1, 0);
        int fromIndex = Math.max(0, toIndex + 1 - limit);
        return list.subList(fromIndex, toIndex);
    }

    public boolean isValid(TicketDTO ticketDTO) {
        if (ticketDTO.getPrice() <= 0) return false;
        if (ticketDTO.getAvailableSeats() <= 0) return false;
        if (ticketDTO.getDepartureCity().equals(ticketDTO.getArrivalCity())) return false;

        LocalDateTime departureDateTime = LocalDateTime.of(ticketDTO.getDepartureDate(), ticketDTO.getDepartureTime());
        LocalDateTime arrivalDateTime = LocalDateTime.of(ticketDTO.getArrivalDate(), ticketDTO.getArrivalTime());
        if (departureDateTime.isAfter(arrivalDateTime)) return false;

        double duration = calculateTravelTimeInHours(departureDateTime, arrivalDateTime);
        if (duration > Ticket.MAX_FLIGHT_DURATION) return false;
        if (ticketDTO.getHours() > 0 && Math.abs(ticketDTO.getHours() - duration) > 1) return false;

        ticketDTO.setHours(duration);
        return true;
    }

    public double calculateTravelTimeInHours(Ticket ticket) {
        return calculateTravelTimeInHours(ticket.getDepartureDateTime(), ticket.getArrivalDateTime());
    }

    public double calculateTravelTimeInHours(LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
        return java.time.Duration.between(departureDateTime, arrivalDateTime).toMinutes() / 60.0;
    }
}