package com.rasim.dax.controller;

import com.rasim.dax.entity.Incident;
import com.rasim.dax.service.IncidentService;
import com.rasim.dax.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
@CrossOrigin("*")
public class IncidentController {

    private final IncidentService incidentService;
    private final JwtService jwtService;

    public IncidentController(
            IncidentService incidentService,
            JwtService jwtService
    ) {

        this.incidentService = incidentService;
        this.jwtService = jwtService;
    }

    @PostMapping
    public Incident createIncident(
            @RequestBody Incident incident,
            @RequestHeader("Authorization") String token
    ) {

        String jwt = token.substring(7);

        Long userId =
                jwtService.extractUserId(jwt);

        String username =
                jwtService.extractUsername(jwt);

        incident.setUserId(userId);

        incident.setCreatedBy(username);

        // PDF PATH
        if (
                incident.getPdfFileName() != null &&
                        !incident.getPdfFileName().isEmpty()
        ) {

            incident.setPdfFilePath(
                    "https://SENIN_BACKEND_LINK/files/download/"
                            + incident.getPdfFileName()
            );
        }

        return incidentService.createIncident(incident);
    }

    @GetMapping
    public List<Incident> getAllIncidents() {

        return incidentService.getAllIncidents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(
            @PathVariable Long id
    ) {

        return incidentService.getIncidentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestHeader("Authorization") String token
    ) {

        String role =
                jwtService.extractRole(token.substring(7));

        Long userId =
                jwtService.extractUserId(token.substring(7));

        Incident incident =
                incidentService.getIncidentById(id)
                        .orElse(null);

        if (incident == null) {

            return ResponseEntity.notFound().build();
        }

        if ("ADMIN".equals(role)) {

            Incident updated =
                    incidentService.updateStatus(id, status);

            return ResponseEntity.ok(updated);
        }

        if (
                incident.getUserId() != null &&
                        incident.getUserId().equals(userId)
        ) {

            Incident updated =
                    incidentService.updateStatus(id, status);

            return ResponseEntity.ok(updated);
        }

        return ResponseEntity
                .status(403)
                .body("Başqasının pozuntusunun statusunu dəyişə bilməzsiniz!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncident(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {

        String role =
                jwtService.extractRole(token.substring(7));

        Long userId =
                jwtService.extractUserId(token.substring(7));

        Incident incident =
                incidentService.getIncidentById(id)
                        .orElse(null);

        if (incident == null) {

            return ResponseEntity.notFound().build();
        }

        if ("ADMIN".equals(role)) {

            incidentService.softDeleteIncident(id);

            return ResponseEntity.ok("Silindi");
        }

        if (
                incident.getUserId() != null &&
                        incident.getUserId().equals(userId)
        ) {

            incidentService.softDeleteIncident(id);

            return ResponseEntity.ok("Silindi");
        }

        return ResponseEntity
                .status(403)
                .body("Başqasının pozuntusunu silə bilməzsiniz!");
    }
}