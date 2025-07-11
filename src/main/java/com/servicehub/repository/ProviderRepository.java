package com.servicehub.repository;

import com.servicehub.model.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Requests, Long>
{
    @Query("SELECT r FROM Requests r WHERE r.status = 'pending' ORDER BY r.createdAt DESC")
    List<Requests> findAllPendingRequestsOrderByCreatedAtDesc();

}
