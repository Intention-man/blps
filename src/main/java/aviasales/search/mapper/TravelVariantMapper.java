package aviasales.search.mapper;

import aviasales.search.data.dto.RouteDTO;
import aviasales.search.data.dto.TravelVariantDTO;
import aviasales.search.data.entity.Route;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TravelVariantMapper {

    private final RouteMapper routeMapper;

    public TravelVariantDTO mapTo(TravelVariant travelVariant) {
        if (travelVariant == null) {
            return null;
        }

        List<RouteDTO> routeDTOs = travelVariant.getRoutes().stream()
                .map(routeMapper::mapTo)
                .collect(Collectors.toList());

        return new TravelVariantDTO(
                travelVariant.getTotalPrice(),
                routeDTOs
        );
    }

    public TravelVariant mapFrom(TravelVariantDTO travelVariantDTO) {
        if (travelVariantDTO == null) {
            return null;
        }

        List<Route> routes = travelVariantDTO.getRoutes().stream()
                .map(routeMapper::mapFrom)
                .collect(Collectors.toList());

        return TravelVariant.builder()
                .totalPrice(travelVariantDTO.getTotalPrice())
                .routes(routes)
                .build();
    }
}
