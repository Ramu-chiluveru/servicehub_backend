package com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.stereotype.Repository;

import com.servicehub.model.Proposal;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ProposalRepo extends JpaRepository<Proposal,Long>
{

 @Query("SELECT p.id FROM Proposal p WHERE p.request.id = :requestId AND p.provider.id = :providerId")
Long getReferenceById(@Param("requestId") String requestId, @Param("providerId") Long providerId);
}
