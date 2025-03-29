package com.example.prac.data.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TravelVariantDTO {
    private double totalPrice;
    private List<RouteDTO> routes;

    public TravelVariantDTO(double totalPrice, List<RouteDTO> routes){
        this.totalPrice = totalPrice;
        this.routes = routes;
    }
}