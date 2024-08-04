package com.example.demo.controller;

import com.example.demo.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/minio")
public class MinioController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/create-bucket")
    public ResponseEntity<String> createBucket(@RequestParam String bucketName) {
        try {
            minioService.createBucket(bucketName);
            return ResponseEntity.ok("Bucket created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam String bucketName, @RequestParam String objectName, @RequestParam MultipartFile file) {
        try (InputStream fileStream = file.getInputStream()) {
            minioService.uploadFile(bucketName, objectName, fileStream, file.getContentType());
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String bucketName, @RequestParam String objectName) {
        try {
            InputStream fileStream = minioService.downloadFile(bucketName, objectName);
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + objectName)
                .body(new InputStreamResource(fileStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
