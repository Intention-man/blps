package com.example.prac.messaging.validator_node;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketReqStatusRepository extends JpaRepository<TicketReqStatus, Long> {
    List<TicketReqStatus> findByJobIdAndTicketRefId(String jobId, String ticketRefId);
    List<TicketReqStatus> findByJobId(String jobId);
}