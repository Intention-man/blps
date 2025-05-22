package com.example.prac.data.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TravelVariantDTO implements Serializable {
    private int totalPrice;
    private List<RouteDTO> routes;

    public TravelVariantDTO(int totalPrice, List<RouteDTO> routes) {
        this.totalPrice = totalPrice;
        this.routes = routes;
    }
}