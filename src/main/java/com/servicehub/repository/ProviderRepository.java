//package com.servicehub.repository;
//
//import com.servicehub.model.Requests;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ProviderRepository extends JpaRepository<Requests, Long>
//{
//    @Query("SELECT r FROM Requests r WHERE r.status = 'pending' ORDER BY r.createdAt DESC")
//    List<Requests> findAllPendingRequestsOrderByCreatedAtDesc();
//
//}
package com.servicehub.repository;

import com.servicehub.dto.ServiceDTO;
import com.servicehub.model.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Requests, Long> {

    @Query(value = """
        SELECT r.* FROM requests r
        JOIN locations l ON r.user_id = l.user_id
        WHERE r.status = 'pending'
        AND ST_DWithin(
            l.location::geography,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
            1
        )
        ORDER BY r.created_at DESC
    """, nativeQuery = true)
    List<Requests> findPendingRequestsNearProvider(
            @Param("lat") double lat,
            @Param("lng") double lng
    );
}