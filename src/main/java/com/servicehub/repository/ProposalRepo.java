package com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.stereotype.Repository;

import com.servicehub.model.Proposal;

@Repository
public interface ProposalRepo extends JpaRepository<Proposal,Long>
{
  

}
