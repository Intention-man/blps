package com.example.prac.data.DTO.simple.res;

import com.example.prac.data.DTO.TicketDTO;
import lombok.Data;

import java.util.List;

@Data
public class FlightLegResDTO {
    private String departureCity;
    private String arrivalCity;
    private int totalTime;
    private List<TicketDTO> tickets;
}
