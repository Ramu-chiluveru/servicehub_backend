package com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicehub.model.Requests;
import java.util.List;
import java.util.Optional;
import com.servicehub.model.Proposal;


@Repository
public interface RequestsRepo extends JpaRepository<Requests,String>
{
   @Query("""
    SELECT r 
    FROM Requests r 
    WHERE r.id NOT IN (
        SELECT p.request.id 
        FROM Proposal p 
        WHERE p.provider.id = :providerid
    )
    ORDER BY r.createdAt DESC
    """)
List<Requests> findPendingRequestsNearProvider(@Param("providerid") Long providerid);


 @Query("""
    SELECT r 
    FROM Requests r 
    WHERE r.id IN (
        SELECT p.request.id 
        FROM Proposal p 
        WHERE p.provider.id = :providerid
    )
    ORDER BY r.createdAt DESC
    """)
List<Requests> findproposalRaisedJobs(@Param("providerid") Long providerid);



}
