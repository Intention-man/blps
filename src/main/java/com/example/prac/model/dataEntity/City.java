package com.example.prac.model.dataEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aviasales_cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}