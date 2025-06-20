package aviasales.data.airline;

import aviasales.data.city.City;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aviasales_airlines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airline_id", nullable = false, unique = true)
    private Long airlineId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airline that = (Airline) o;
        return airlineId != null && airlineId.equals(that.airlineId);
    }

    @Override
    public int hashCode() {
        return airlineId != null ? airlineId.hashCode() : 0;
    }
}
