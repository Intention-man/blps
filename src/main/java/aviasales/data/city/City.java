package aviasales.data.city;

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
    @Column(name = "city_id", nullable = false, unique = true)
    private Long cityId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public City(String cityName) {
        this.name = cityName;
    }
}