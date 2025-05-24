package aviasales.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import aviasales.common.data.entity.Airline;
import aviasales.common.data.entity.City;
import aviasales.common.data.entity.ServiceClass;
import aviasales.common.data.entity.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.serviceClass = :serviceClass " +
            "AND t.availableSeats >= :passengerCount " +
            "AND t.airline IN :availableAirlines " +
            "AND t.price <= :maxPrice " +
            "AND t.hours <= :maxTravelTime " +
            "AND t.departureCity = :departureCity " +
            "AND t.departureDate BETWEEN :departureDateStart AND :departureDateFinish " +
            "AND t.departureTime BETWEEN :departureTimeStart AND :departureTimeFinish"
    )
    List<Ticket> findFirstTickets(
            @Param("serviceClass") ServiceClass serviceClass,
            @Param("passengerCount") int passengerCount,
            @Param("maxPrice") int maxPrice,
            @Param("maxTravelTime") double maxTravelTime,
            @Param("availableAirlines") List<Airline> availableAirlines,
            @Param("departureCity") City departureCity,
            @Param("departureDateStart") LocalDate departureDateStart,
            @Param("departureDateFinish") LocalDate departureDateFinish,
            @Param("departureTimeStart") LocalTime departureTimeStart,
            @Param("departureTimeFinish") LocalTime departureTimeFinish
    );

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.serviceClass = :serviceClass " +
            "AND t.availableSeats >= :passengerCount " +
            "AND t.airline IN :availableAirlines " +
            "AND t.price <= :maxPrice " +
            "AND t.hours <= :maxTravelTime " +
            "AND t.departureCity = :departureCity " +
            "AND t.departureDateTime >= :minDepartureStartDatetime " +
            "AND t.arrivalDateTime <= :maxArrivalFinishDatetime"
    )
    List<Ticket> findIntermediateTickets(
            @Param("serviceClass") ServiceClass serviceClass,
            @Param("passengerCount") int passengerCount,
            @Param("maxPrice") int maxPrice,
            @Param("maxTravelTime") double maxTravelTime,
            @Param("availableAirlines") List<Airline> availableAirlines,
            @Param("departureCity") City departureCity,
            @Param("minDepartureStartDatetime") LocalDateTime minDepartureStartDatetime,
            @Param("maxArrivalFinishDatetime") LocalDateTime maxArrivalFinishDatetime
    );

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.serviceClass = :serviceClass " +
            "AND t.availableSeats >= :passengerCount " +
            "AND t.airline IN :availableAirlines " +
            "AND t.price <= :maxPrice " +
            "AND t.hours <= :maxTravelTime " +
            "AND t.departureCity = :departureCity " +
            "AND t.departureDate BETWEEN :departureDateStart AND :departureDateFinish " +
            "AND t.departureTime BETWEEN :departureTimeStart AND :departureTimeFinish " +
            "AND t.arrivalCity = :arrivalCity " +
            "AND t.arrivalDate BETWEEN :arrivalDateStart AND :arrivalDateFinish " +
            "AND t.arrivalTime BETWEEN :arrivalTimeStart AND :arrivalTimeFinish " +
            "AND t.departureDateTime >= :minDepartureStartDatetime " +
            "AND t.arrivalDateTime <= :maxArrivalFinishDatetime"
    )
    List<Ticket> findFinishTickets(
            @Param("serviceClass") ServiceClass serviceClass,
            @Param("passengerCount") int passengerCount,
            @Param("maxPrice") int maxPrice,
            @Param("maxTravelTime") double maxTravelTime,
            @Param("availableAirlines") List<Airline> availableAirlines,
            @Param("departureCity") City departureCity,
            @Param("departureDateStart") LocalDate departureDateStart,
            @Param("departureDateFinish") LocalDate departureDateFinish,
            @Param("departureTimeStart") LocalTime departureTimeStart,
            @Param("departureTimeFinish") LocalTime departureTimeFinish,
            @Param("arrivalCity") City arrivalCity,
            @Param("arrivalDateStart") LocalDate arrivalDateStart,
            @Param("arrivalDateFinish") LocalDate arrivalDateFinish,
            @Param("arrivalTimeStart") LocalTime arrivalTimeStart,
            @Param("arrivalTimeFinish") LocalTime arrivalTimeFinish,
            @Param("minDepartureStartDatetime") LocalDateTime minDepartureStartDatetime,
            @Param("maxArrivalFinishDatetime") LocalDateTime maxArrivalFinishDatetime
    );

    // NOTE может пригодиться, если захочу вначале делать sql запрос на создание представления,
    //  которое будет отфильтрованной таблицей Tickets по некоторым параметрам,
    //  по которым можно отфильтровать уже в начале поиска.
    //  А потом можно искать билеты на каждом этапе в этом VIEW, а не во всей таблице.
    //  По идее это сократит время в несколько раз
    @Query("SELECT t FROM Ticket t " +
            "WHERE t.serviceClass = :serviceClass " +
            "AND t.availableSeats >= :passengerCount " +
            "AND t.price <= :maxPrice")
    List<Ticket> findAllMaybeSuitableTickets(
            @Param("serviceClass") ServiceClass serviceClass,
            @Param("passengerCount") int passengerCount,
            @Param("maxPrice") int maxPrice
    );

    @Query("SELECT t FROM Ticket t")
    List<Ticket> getAllTickets();
}