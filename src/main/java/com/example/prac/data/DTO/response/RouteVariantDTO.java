package com.example.prac.data.DTO.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RouteVariantDTO {
    private Integer totalPrice;
    private List<FlightLegResDTO> flightLegs;

    public RouteVariantDTO(){
        this.totalPrice = 0;
        this.flightLegs = new ArrayList<FlightLegResDTO>();
    }
}