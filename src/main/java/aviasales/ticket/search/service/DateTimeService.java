package aviasales.ticket.search.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import aviasales.ticket.search.data.dto.ComplexRouteLegDTO;
import aviasales.ticket.search.data.dto.ComplexTravelSearchRequestDTO;
import aviasales.ticket.search.data.dto.SimpleTravelSearchRequestDTO;
import aviasales.common.errorHandler.ExceededMaxComplexRouteLegsException;
import aviasales.common.errorHandler.TravelDateTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class DateTimeService {

    public void validate(SimpleTravelSearchRequestDTO dto) {
        // Основной маршрут
        if (dto.getDepartureDateStart() != null && dto.getDepartureDateFinish() != null &&
                dto.getDepartureDateStart().isAfter(dto.getDepartureDateFinish())) {
            throw new TravelDateTimeException("Дата начала отправления не может быть позже даты окончания.");
        }

        if (dto.getDepartureTimeStart() != null && dto.getDepartureTimeFinish() != null &&
                dto.getDepartureTimeStart().isAfter(dto.getDepartureTimeFinish())) {
            throw new TravelDateTimeException("Время начала отправления не может быть позже времени окончания.");
        }

        if (dto.getArrivalDateStart() != null && dto.getArrivalDateFinish() != null &&
                dto.getArrivalDateStart().isAfter(dto.getArrivalDateFinish())) {
            throw new TravelDateTimeException("Дата начала прибытия не может быть позже даты окончания.");
        }

        if (dto.getArrivalTimeStart() != null && dto.getArrivalTimeFinish() != null &&
                dto.getArrivalTimeStart().isAfter(dto.getArrivalTimeFinish())) {
            throw new TravelDateTimeException("Время начала прибытия не может быть позже времени окончания.");
        }

        if (dto.getDepartureDateStart() != null && dto.getDepartureTimeStart() != null &&
                dto.getArrivalDateFinish() != null && dto.getArrivalTimeFinish() != null) {
            LocalDateTime departureDateTime = LocalDateTime.of(dto.getDepartureDateStart(), dto.getDepartureTimeStart());
            LocalDateTime arrivalDateTime = LocalDateTime.of(dto.getArrivalDateFinish(), dto.getArrivalTimeFinish());
            if (departureDateTime.isAfter(arrivalDateTime)) {
                throw new TravelDateTimeException("Дата и время прибытия должны быть позже или равны дате и времени отправления.");
            }
        }

        // Обратный маршрут
        if (dto.getBackDepartureDateStart() != null && dto.getBackDepartureDateFinish() != null &&
                dto.getBackDepartureDateStart().isAfter(dto.getBackDepartureDateFinish())) {
            throw new TravelDateTimeException("Дата начала обратного отправления не может быть позже даты окончания.");
        }

        if (dto.getBackDepartureTimeStart() != null && dto.getBackDepartureTimeFinish() != null &&
                dto.getBackDepartureTimeStart().isAfter(dto.getBackDepartureTimeFinish())) {
            throw new TravelDateTimeException("Время начала обратного отправления не может быть позже времени окончания.");
        }

        if (dto.getBackArrivalDateStart() != null && dto.getBackArrivalDateFinish() != null &&
                dto.getBackArrivalDateStart().isAfter(dto.getBackArrivalDateFinish())) {
            throw new TravelDateTimeException("Дата начала обратного прибытия не может быть позже даты окончания.");
        }

        if (dto.getBackArrivalTimeStart() != null && dto.getBackArrivalTimeFinish() != null &&
                dto.getBackArrivalTimeStart().isAfter(dto.getBackArrivalTimeFinish())) {
            throw new TravelDateTimeException("Время начала обратного прибытия не может быть позже времени окончания.");
        }

        if (dto.getBackDepartureDateStart() != null && dto.getBackDepartureTimeStart() != null &&
                dto.getBackArrivalDateFinish() != null && dto.getBackArrivalTimeFinish() != null) {
            LocalDateTime backDepartureDateTime = LocalDateTime.of(dto.getBackDepartureDateStart(), dto.getBackDepartureTimeStart());
            LocalDateTime backArrivalDateTime = LocalDateTime.of(dto.getBackArrivalDateFinish(), dto.getBackArrivalTimeFinish());
            if (backDepartureDateTime.isAfter(backArrivalDateTime)) {
                throw new TravelDateTimeException("Дата и время обратного прибытия должны быть позже или равны дате и времени обратного отправления.");
            }
        }

        if (dto.getArrivalDateFinish() != null && dto.getBackDepartureDateStart() != null &&
                dto.getArrivalDateFinish().isAfter(dto.getBackDepartureDateStart())) {
            throw new TravelDateTimeException("Обратное отправление не может начинаться до завершения основного маршрута.");
        }
    }

    public void validate(ComplexTravelSearchRequestDTO dto) {
        List<ComplexRouteLegDTO> complexRouteLegs = dto.getComplexRouteLegs();
        if (complexRouteLegs.size() > 6) {
            throw new ExceededMaxComplexRouteLegsException();
        }
        validate(complexRouteLegs);
    }

    private void validate(List<ComplexRouteLegDTO> list) {
        for (ComplexRouteLegDTO dto : list) {
            if (dto.getDepartureTimeStart() != null && dto.getDepartureTimeFinish() != null &&
                    dto.getDepartureTimeStart().isAfter(dto.getDepartureTimeFinish())) {
                throw new TravelDateTimeException("Время начала отправления не может быть позже времени окончания.");
            }

            if (dto.getArrivalTimeStart() != null && dto.getArrivalTimeFinish() != null &&
                    dto.getArrivalTimeStart().isAfter(dto.getArrivalTimeFinish())) {
                throw new TravelDateTimeException("Время начала прибытия не может быть позже времени окончания.");
            }
        }

        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getDepartureDate().isAfter(list.get(i + 1).getDepartureDate())) {
                throw new TravelDateTimeException("Дата вылета не может быть позже даты вылета следующего этапа маршрута");
            }
        }

    }

    public LocalDate max(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }

    public LocalDateTime max(LocalDateTime dt1, LocalDateTime dt2) {
        return dt1.isAfter(dt2) ? dt1 : dt2;
    }
}
