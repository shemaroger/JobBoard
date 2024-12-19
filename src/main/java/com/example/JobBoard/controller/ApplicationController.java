package com.example.JobBoard.controller;

import com.example.JobBoard.model.Application;
import com.example.JobBoard.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    private static final String UPLOAD_DIR = "uploads";
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply")
    public String handleFileUpload(
            @RequestParam("jobId") Long jobId,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "status", required = false) String status,  // Make 'status' optional
            @RequestParam("files") MultipartFile[] files) {

        if (jobId == null || jobId <= 0) {
            return "Invalid jobId provided.";
        }

        try {
            // Default status if not provided
            if (status == null || status.isEmpty()) {
                status = "Pending";  // You can change this to any default status
            }

            // Create the upload directory if it does not exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            List<String> fileUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileUrl = storeFile(file);
                    fileUrls.add(fileUrl);
                } else {
                    return "One or more files are empty. Please upload valid files.";
                }
            }

            Application application = new Application();
            application.setFullName(fullName);
            application.setEmail(email);
            application.setPhoneNumber(phoneNumber);
            application.setStatus(status);
            application.setFileUrls(fileUrls);

            applicationService.saveApplication(application, jobId);

            return "Application submitted successfully with files.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload files: " + e.getMessage();
        }
    }


    private String storeFile(MultipartFile file) throws IOException {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(file.getInputStream(), filePath);

        return "http://localhost:8080/files/" + fileName;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId));
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }
}
