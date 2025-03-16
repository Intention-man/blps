package com.example.prac.DTO.data;

import lombok.Data;

import java.util.List;

@Data
public class TicketSearchResponseDTO {
    private List<RouteOptionDTO> routeOptions;
}