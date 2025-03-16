package com.example.prac.data.DTO.response;

import lombok.Data;

import java.util.List;

@Data
public class TicketSearchResponse {
    private List<RouteVariantDTO> routeOptions;
}