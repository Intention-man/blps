package com.example.prac.data.res;

import lombok.Data;

import java.util.List;

@Data
public class TicketSearchResponseDTO {
    private int variantsCount;
    private List<TravelVariantDTO> variants;
}