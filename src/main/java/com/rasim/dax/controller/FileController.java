package com.rasim.dax.controller;

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

    private final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fayl boşdur");
            }

            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body("Yalnız PDF faylları yüklənə bilər");
            }

            // Faylın adını unikal et
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Qovluğu yarat (əgər yoxdursa)
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Faylı saxla
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok(fileName);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Fayl yüklənmədi: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
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