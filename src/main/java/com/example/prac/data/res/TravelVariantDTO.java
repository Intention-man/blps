package com.example.prac.data.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TravelVariantDTO {
    private Integer totalPrice;
    private List<RouteDTO> routes;

    public TravelVariantDTO(){
        this.totalPrice = 0;
        this.routes = new ArrayList<>();
    }
}