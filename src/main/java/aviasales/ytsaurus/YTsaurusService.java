package aviasales.ytsaurus;

import aviasales.search.service.TicketSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class YTsaurusService {
    private final YTsaurusConnectionFactory yTsaurusConnectionFactory;
    private final TicketSearchService ticketSearchService;

    public void addStatistics() throws Exception {
        YTsaurusConnection connection = null;
        try {
            connection = yTsaurusConnectionFactory.getConnection();
            connection.addStatisticRow(timestampNowToLong(), ticketSearchService.countTickets());
        } catch (Exception e) {
            throw e;
        } finally {
            assert connection != null;
            yTsaurusConnectionFactory.closeConnection(connection);
        }
    }

    long timestampNowToLong() {
        Timestamp timestamp = Timestamp.from(Instant.now());
        return timestamp.getTime() * 1000 + timestamp.getNanos() / 1000;
    }
}