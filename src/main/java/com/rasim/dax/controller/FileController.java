package com.rasim.dax.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    private final String UPLOAD_DIR =
            "/data/uploads/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {

        try {

            if (file.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("Fayl boşdur");
            }

            if (
                    file.getContentType() == null ||
                            !file.getContentType()
                                    .equals("application/pdf")
            ) {

                return ResponseEntity
                        .badRequest()
                        .body("Yalnız PDF faylları yüklənə bilər");
            }

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + file.getOriginalFilename();

            Path uploadPath =
                    Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {

                Files.createDirectories(uploadPath);
            }

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.write(
                    filePath,
                    file.getBytes()
            );

            return ResponseEntity.ok(fileName);

        } catch (IOException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Fayl yüklənmədi: "
                                    + e.getMessage()
                    );
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String fileName
    ) {

        try {

            Path filePath =
                    Paths.get(
                            UPLOAD_DIR,
                            fileName
                    );

            if (!Files.exists(filePath)) {

                return ResponseEntity
                        .notFound()
                        .build();
            }

            byte[] fileBytes =
                    Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.APPLICATION_PDF
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\""
                                    + fileName
                                    + "\""
                    )
                    .body(fileBytes);

        } catch (IOException e) {

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}