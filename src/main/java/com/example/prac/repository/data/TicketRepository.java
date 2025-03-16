package com.example.prac.repository.data;

import com.example.prac.model.dataEntity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.departureCity = :departureCity AND t.arrivalCity = :arrivalCity AND t.departureTime BETWEEN :startTime AND :endTime")
    List<Ticket> findByDepartureCityAndArrivalCityAndDepartureTimeBetween(
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT t FROM Ticket t WHERE t.departureCity = :departureCity AND t.arrivalCity = :arrivalCity AND t.departureTime > :startTime")
    List<Ticket> findByDepartureCityAndArrivalCityAndDepartureTimeAfter(
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity,
            @Param("startTime") LocalDateTime startTime);

    @Query("SELECT t FROM Ticket t")
    List<Ticket> getAllTickets();
}