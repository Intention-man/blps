package com.example.prac.data.res;

import lombok.Data;

import java.util.List;

@Data
public class TicketSearchResponse {
    private List<TravelVariantDTO> routeOptions;
}