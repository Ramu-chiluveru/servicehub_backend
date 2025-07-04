package com.servicehub.repository;

import com.servicehub.dto.ProposalDTO;
import com.servicehub.model.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Requests, Long> 
{
    List<Requests> findByUserId(Long userId);

    List<ProposalDTO> findWithProposalsById(Long requestId);
}
