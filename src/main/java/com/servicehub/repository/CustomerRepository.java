package com.servicehub.repository;

import com.servicehub.dto.ProposalDTO;
import com.servicehub.model.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Requests, Long> 
{
    List<Requests> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<ProposalDTO> findWithProposalsById(String requestId);

    @Query("SELECT COUNT(r) FROM Requests r WHERE DATE(r.createdAt) = :today")
    int countByCreatedAtDate(@Param("today") LocalDate today);

    @Query("SELECT r FROM Requests r WHERE id = :id")
    Requests findById(String id);
}
