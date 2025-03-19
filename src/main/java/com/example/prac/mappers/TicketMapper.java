package com.example.prac.mappers;

import com.example.prac.data.DTO.TicketDTO;
import com.example.prac.data.model.Ticket;
import com.example.prac.service.CityService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TicketMapper implements Mapper<Ticket, TicketDTO> {

    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final CityMapper cityMapper;

    @Override
    public TicketDTO mapTo(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        TicketDTO ticketDTO = modelMapper.map(ticket, TicketDTO.class);

        ticketDTO.setDepartureCity(cityMapper.mapToName(ticket.getDepartureCity()));
        ticketDTO.setArrivalCity(cityMapper.mapToName(ticket.getArrivalCity()));

        return ticketDTO;
    }

    @Override
    public Ticket mapFrom(TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return null;
        }

        Ticket ticket = modelMapper.map(ticketDTO, Ticket.class);

        ticket.setDepartureCity(cityService.findOrCreateCity(ticketDTO.getDepartureCity()));
        ticket.setArrivalCity(cityService.findOrCreateCity(ticketDTO.getArrivalCity()));

        return ticket;
    }
}