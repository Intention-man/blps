package com.example.prac.data.res;

import lombok.Data;

import java.io.Serializable;

@Data
public class CityDTO implements Serializable {
    private Long id;
    private String name;
}