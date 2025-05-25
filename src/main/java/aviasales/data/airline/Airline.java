package aviasales.data.airline;

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
}
