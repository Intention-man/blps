package com.example.prac.data.DTO.simple.res;

import lombok.Data;

import java.util.List;

@Data
public class TicketSearchResponse {
    private List<RouteVariantDTO> routeOptions;
}