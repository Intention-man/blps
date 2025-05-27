package aviasales.management.generation;

import aviasales.data.airline.Airline;
import aviasales.data.airline.AirlineRepository;
import aviasales.data.city.City;
import aviasales.data.city.CityService;
import aviasales.data.ticket.ServiceClass;
import aviasales.data.ticket.Ticket;
import aviasales.data.ticket.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static aviasales.Utils.calculateDiffInHours;

@Service
@AllArgsConstructor
public class GenerationService {
    private TicketRepository ticketRepository;
    private CityService cityService;
    private AirlineRepository airlineRepository;

    public void generateAndSaveTickets(LocalDate departureDate) {
        List<Ticket> tickets = new ArrayList<>();
        Random random = new Random();

        List<City> cities = cityService.getAllCities();
        if (cities.isEmpty()) {
            System.out.println("No cities found in the database");
            return;
        }

        List<Airline> airlines = airlineRepository.findAll();
        if (airlines.isEmpty()) {
            System.out.println("No airlines found in the database");
            return;
        }

        for (Airline airline : airlines) {
            for (City departureCity : cities) {
                List<City> otherCities = new ArrayList<>(cities);
                otherCities.remove(departureCity);
                for (City arrivalCity : otherCities) {
                    ServiceClass serviceClass = ServiceClass.values()[random.nextInt(ServiceClass.values().length)];
                    String flightNumber = String.format("%s%03d", airline.getName().substring(0, 2).toUpperCase(), random.nextInt(900) + 100);

//                    LocalDate departureDate = LocalDate.now().plusDays(random.nextInt(2));
//                    LocalDate departureDate = LocalDate.of(2025, 3, 28).plusDays(random.nextInt(2));

                    LocalTime departureTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
                    LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);

                    LocalDateTime arrivalDateTime = departureDateTime
                            .plusHours(1 + random.nextInt(10))
                            .plusMinutes(random.nextInt(60));
                    LocalDate arrivalDate = arrivalDateTime.toLocalDate();
                    LocalTime arrivalTime = arrivalDateTime.toLocalTime();

                    int price = random.nextInt(400) + 100;
                    int availableSeats = random.nextInt(3) + 1;

                    Ticket ticket = Ticket.builder()
                            .airline(airline)
                            .serviceClass(serviceClass)
                            .flightNumber(flightNumber)
                            .departureCity(departureCity)
                            .departureDate(departureDate)
                            .departureTime(departureTime)
                            .departureDateTime(departureDateTime)
                            .arrivalCity(arrivalCity)
                            .arrivalDate(arrivalDate)
                            .arrivalTime(arrivalTime)
                            .arrivalDateTime(arrivalDateTime)
                            .price(price)
                            .availableSeats(availableSeats)
                            .build();
                    ticket.setHours(calculateTravelTimeInHours(ticket));

                    tickets.add(ticket);
                }
            }
        }
        ticketRepository.saveAll(tickets);
    }

    public void generateAndSaveLimitedTickets(LocalDate departureDate) {
        List<Ticket> tickets = new ArrayList<>();
        Random random = new Random();

        List<City> cities = cityService.getAllCities();
        if (cities.isEmpty()) {
            System.out.println("No cities found in the database");
            return;
        }

        List<Airline> airlines = airlineRepository.findAll();
        if (airlines.isEmpty()) {
            System.out.println("No airlines found in the database");
            return;
        }

        for (City departureCity : cities) {
            List<City> otherCities = new ArrayList<>(cities);
            otherCities.remove(departureCity);

            for (City arrivalCity : otherCities) {
                Airline airline = airlines.get(random.nextInt(airlines.size()));
                ServiceClass serviceClass = ServiceClass.values()[random.nextInt(ServiceClass.values().length)];

                String flightNumber = String.format("%s%03d",
                        airline.getName().substring(0, 2).toUpperCase(),
                        random.nextInt(900) + 100);

                LocalTime departureTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
                LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);

                LocalDateTime arrivalDateTime = departureDateTime
                        .plusHours(1 + random.nextInt(10))
                        .plusMinutes(random.nextInt(60));

                Ticket ticket = Ticket.builder()
                        .airline(airline)
                        .serviceClass(serviceClass)
                        .flightNumber(flightNumber)
                        .departureCity(departureCity)
                        .departureDate(departureDate)
                        .departureTime(departureTime)
                        .departureDateTime(departureDateTime)
                        .arrivalCity(arrivalCity)
                        .arrivalDate(arrivalDateTime.toLocalDate())
                        .arrivalTime(arrivalDateTime.toLocalTime())
                        .arrivalDateTime(arrivalDateTime)
                        .price(100 + random.nextInt(400))
                        .availableSeats(1 + random.nextInt(3))
                        .build();

                ticket.setHours(calculateTravelTimeInHours(ticket));
                tickets.add(ticket);
            }
        }

        ticketRepository.saveAll(tickets);
    }

    public double calculateTravelTimeInHours(Ticket ticket) {
        return calculateDiffInHours(ticket.getDepartureDateTime(), ticket.getArrivalDateTime());
    }
}