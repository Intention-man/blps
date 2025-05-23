package com.example.prac.messaging.validator_node;

import com.example.prac.data.model.Ticket;
import com.example.prac.data.res.TicketDTO;
import com.example.prac.messaging.dto.ValidationResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TicketValidator {
    public ValidationResult validate(TicketDTO ticketDTO) {
        if (ticketDTO.getPrice() <= 0) return new ValidationResult(false, "Price must be positive");
        if (ticketDTO.getAvailableSeats() <= 0) return new ValidationResult(false, "Available seats must be positive");
        if (ticketDTO.getDepartureCity().equals(ticketDTO.getArrivalCity())) return new ValidationResult(false, "Departure and arrival city cannot be the same");

        LocalDateTime departureDateTime = LocalDateTime.of(ticketDTO.getDepartureDate(), ticketDTO.getDepartureTime());
        LocalDateTime arrivalDateTime = LocalDateTime.of(ticketDTO.getArrivalDate(), ticketDTO.getArrivalTime());
        if (departureDateTime.isAfter(arrivalDateTime)) return new ValidationResult(false, "arrivalDateTime must be after departureDateTime");

        double duration = java.time.Duration.between(departureDateTime, arrivalDateTime).toMinutes() / 60.0;
        if (duration > Ticket.MAX_FLIGHT_DURATION) return new ValidationResult(false, "duration must be less than or equal to " + Ticket.MAX_FLIGHT_DURATION + " hours");
        if (ticketDTO.getHours() > 0 && Math.abs(ticketDTO.getHours() - duration) > 1) return new ValidationResult(false, "Incorrect data");

        return new ValidationResult(true, "OK");
    }
}