package com.example.prac.repository;

import com.example.prac.data.model.City;
import com.example.prac.data.model.ServiceClass;
import com.example.prac.data.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t")
    List<Ticket> getAllTickets();

    @Query("SELECT t FROM Ticket t " +
            "WHERE t.serviceClass = :serviceClass " +
            "AND t.departureCity = :departureCity " +
            "AND t.departureDate BETWEEN :departureDateStart AND :departureDateFinish " +
            "AND t.departureTime BETWEEN :departureTimeStart AND :departureTimeFinish")
    List<Ticket> findTicketsByDepartureData(
            @Param("serviceClass") ServiceClass serviceClass,
            @Param("departureCity") City departureCity,
            @Param("departureDateStart") LocalDate departureDateStart,
            @Param("departureDateFinish") LocalDate departureDateFinish,
            @Param("departureTimeStart") LocalTime departureTimeStart,
            @Param("departureTimeFinish") LocalTime departureTimeFinish);
}