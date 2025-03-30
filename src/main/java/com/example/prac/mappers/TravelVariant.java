package com.example.prac.mappers;

import com.example.prac.data.model.Route;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelVariant {
    private int totalPrice;
    private List<Route> routes;
}
