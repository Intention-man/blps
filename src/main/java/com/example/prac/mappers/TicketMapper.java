package com.example.prac.mappers;

import com.example.prac.data.model.ServiceClass;
import com.example.prac.data.res.TicketDTO;
import com.example.prac.data.model.Ticket;
import com.example.prac.service.AirlineService;
import com.example.prac.service.CityService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TicketMapper implements Mapper<Ticket, TicketDTO> {
    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final AirlineService airlineService;
    private final CityMapper cityMapper;

    @Override
    public TicketDTO mapTo(Ticket ticket) {
        if (ticket == null) return null;

        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);
        ticketDTO.setAirline(ticket.getAirline().getName());
        ticketDTO.setDepartureCity(cityMapper.mapToName(ticket.getDepartureCity()));
        ticketDTO.setArrivalCity(cityMapper.mapToName(ticket.getArrivalCity()));
        return ticketDTO;
    }

    @Override
    public Ticket mapFrom(TicketDTO ticketDTO) {
        if (ticketDTO == null) return null;

        return Ticket.builder()
                .airline(airlineService.findByName(ticketDTO.getAirline()))
                .serviceClass(ServiceClass.valueOf(ticketDTO.getServiceClass()))
                .price(ticketDTO.getPrice())
                .availableSeats(ticketDTO.getAvailableSeats())
                .flightNumber(ticketDTO.getFlightNumber())
                .departureCity(cityService.findByName(ticketDTO.getDepartureCity()))
                .departureDate(ticketDTO.getDepartureDate())
                .departureTime(ticketDTO.getDepartureTime())
                .departureDateTime(LocalDateTime.of(ticketDTO.getDepartureDate(), ticketDTO.getDepartureTime()))
                .arrivalCity(cityService.findByName(ticketDTO.getArrivalCity()))
                .arrivalDate(ticketDTO.getArrivalDate())
                .arrivalTime(ticketDTO.getArrivalTime())
                .arrivalDateTime(LocalDateTime.of(ticketDTO.getArrivalDate(), ticketDTO.getArrivalTime()))
                .hours(ticketDTO.getHours())
                .build();
    }
}