package aviasales.ticket.management.messaging.validator_node;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import aviasales.ticket.management.messaging.dto.TicketValidationStatus;

import java.util.List;

@Repository
public interface TicketReqStatusRepository extends JpaRepository<TicketReqStatus, Long> {
    List<TicketReqStatus> findByJobId(String jobId);

    List<TicketReqStatus> findByJobIdAndStatus(String jobId, TicketValidationStatus status);

    @Modifying
    @Query("UPDATE TicketReqStatus t SET t.status = :status WHERE t.ticketRefId = :ticketRefId")
    void updateStatusByTicketRefId(
            @Param("ticketRefId") String ticketRefId,
            @Param("status") TicketValidationStatus status
    );
}