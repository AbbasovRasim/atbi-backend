package com.rasim.dax.controller;

import com.rasim.dax.entity.Incident;
import com.rasim.dax.service.IncidentService;
import com.rasim.dax.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
public class FileController {

    private final String UPLOAD_DIR = "/tmp/uploads/";
    private final JwtService jwtService;
    private final IncidentService incidentService;

    public FileController(JwtService jwtService, IncidentService incidentService) {
        this.jwtService = jwtService;
        this.incidentService = incidentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fayl boşdur");
            }

            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body("Yalnız PDF faylları yüklənə bilər");
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok(fileName);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Fayl yüklənmədi: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(
            @PathVariable String fileName,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        // 1. TOKEN YOXLANIŞI
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Zəhmət olmasa, daxil olun");
        }

        String token = authHeader.substring(7);
        String role = jwtService.extractRole(token);
        Long currentUserId = jwtService.extractUserId(token);

        // 2. FAYLIN HANSI İNCİDENTƏ AİT OLDUĞUNU TAP
        Incident incident = incidentService.findIncidentByPdfFileName(fileName);
        if (incident == null) {
            return ResponseEntity.status(404).body("Fayl tapılmadı");
        }

        // 3. İCAZƏ YOXLANIŞI: ADMIN və ya ÖZ sənədi
        if (!"ADMIN".equals(role) && !incident.getUserId().equals(currentUserId)) {
            return ResponseEntity.status(403).body("Bu faylı açmağa icazəniz yoxdur");
        }

        // 4. FAYLI OXU VƏ GÖNDƏR
        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            if (!Files.exists(filePath)) {
                return ResponseEntity.status(404).body("Fayl tapılmadı");
            }

            byte[] fileBytes = Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "inline; filename=" + fileName)
                    .body(fileBytes);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Fayl oxunmadı: " + e.getMessage());
        }
    }
}