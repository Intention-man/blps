package aviasales.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import aviasales.common.data.entity.Airline;

import java.util.Optional;


@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Optional<Airline> findByName(String name);
}