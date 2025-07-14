package com.servicehub.repository;

import com.servicehub.model.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationsRepository extends JpaRepository<Locations, Long> {
    Optional<Locations> findTopByUserIdOrderByIdDesc(Long userId);
}
