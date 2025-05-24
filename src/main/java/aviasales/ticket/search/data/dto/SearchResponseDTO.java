package aviasales.ticket.search.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchResponseDTO implements Serializable {
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