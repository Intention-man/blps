package com.example.prac.data.res;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponseDTO {
    private int variantsCount;
    private List<TravelVariantDTO> variants;
}