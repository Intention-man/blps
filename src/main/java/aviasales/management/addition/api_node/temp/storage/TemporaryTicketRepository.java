package aviasales.management.addition.api_node.temp.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TemporaryTicketRepository extends JpaRepository<TemporaryTicket, String> {
    Optional<TemporaryTicket> findById(String id);

    List<TemporaryTicket> findByJobId(String jobId);

    void deleteById(String id);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);

    void deleteByJobId(String jobId);
}
