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

    // ✅ Render.com üçün /tmp qovluğu
    private final String UPLOAD_DIR = "/tmp/uploads/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== UPLOAD START ===");
            System.out.println("File: " + file.getOriginalFilename());
            System.out.println("Size: " + file.getSize() + " bytes");

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Fayl boşdur");
            }

            // Fayl adı
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // Qovluğu yarat
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created: " + uploadPath.toAbsolutePath());
            }

            // Faylı saxla
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());
            System.out.println("Saved: " + filePath.toAbsolutePath());

            return ResponseEntity.ok(fileName);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
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
            return ResponseEntity.status(500).body("Fayl oxunmadı");
        }
    }
}