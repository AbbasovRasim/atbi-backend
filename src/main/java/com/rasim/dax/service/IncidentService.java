package com.rasim.dax.service;

import com.rasim.dax.entity.Incident;
import com.rasim.dax.repository.IncidentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    // INCIDENT YARAT
    public Incident createIncident(Incident incident) {


        return incidentRepository.save(incident);
    }

    // SİLİNMƏYƏNLƏRİ GÖSTƏR
    public List<Incident> getAllIncidents() {
        return incidentRepository.findByDeletedFalse();
    }

    public Optional<Incident> getIncidentById(Long id) {
        return incidentRepository.findById(id);
    }

    public void softDeleteIncident(Long id) {
        Incident incident = incidentRepository.findById(id).orElseThrow();

        incident.setDeleted(true);

        incidentRepository.save(incident);
    }

    public Incident updateStatus(Long id, String status) {

        Incident incident = incidentRepository.findById(id).orElseThrow();

        incident.setStatus(status);

        return incidentRepository.save(incident);
    }
}