package com.rasim.dax.repository;

import com.rasim.dax.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByDeletedFalse();  // ✅ BU METOD OLMALIDIR!
}