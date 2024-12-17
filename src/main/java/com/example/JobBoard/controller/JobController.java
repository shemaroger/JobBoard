package com.example.JobBoard.controller;

import com.example.JobBoard.model.Job;
import com.example.JobBoard.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // Create a new job
    @PostMapping("/add")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.createJob(job));
    }

    // Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all jobs
    @GetMapping("/all")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // Get jobs by category ID
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Job>> getJobsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(jobService.getJobsByCategory(categoryId));
    }

    // Get jobs by employer ID
    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<Job>> getJobsByEmployer(@PathVariable Long employerId) {
        return ResponseEntity.ok(jobService.getJobsByEmployer(employerId));
    }

    // Get jobs by location
    @GetMapping("/location")
    public ResponseEntity<List<Job>> getJobsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(jobService.getJobsByLocation(location));
    }

    // Update a job by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        Job updatedJob = jobService.updateJob(id, jobDetails);
        return ResponseEntity.ok(updatedJob);
    }
    // Search jobs globally
    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String keyword) {
        return ResponseEntity.ok(jobService.searchJobs(keyword));
    }

    // Delete a job by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
