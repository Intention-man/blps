package com.example.prac.data.res;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponseDTO {
    private int variantsCount;
    private int startIndex;
    private int endIndex;
    private List<TravelVariantDTO> variants;

    public SearchResponseDTO(int size, List<TravelVariantDTO> travelVariantDTOStream, int startIndex, int endIndex) {
        this.variantsCount = size;
        this.variants = travelVariantDTOStream;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public SearchResponseDTO(int size, List<TravelVariantDTO> travelVariantDTOStream) {
        this.variantsCount = size;
        this.variants = travelVariantDTOStream;
    }
}