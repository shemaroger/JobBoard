package com.example.JobBoard.service;

import com.example.JobBoard.model.Job;
import com.example.JobBoard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // Create a new job
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    // Get a job by its ID
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get jobs by category ID
    public List<Job> getJobsByCategory(Long categoryId) {
        return jobRepository.findByCategory_Id(categoryId);
    }

    // Get jobs by employer ID
    public List<Job> getJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployer_Id(employerId);
    }

    // Get jobs by location
    public List<Job> getJobsByLocation(String location) {
        return jobRepository.findByLocationContaining(location);
    }

    // Update a job
    public Job updateJob(Long id, Job jobDetails) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id " + id));

        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setLocation(jobDetails.getLocation());
        job.setEmploymentType(jobDetails.getEmploymentType());
        job.setSkillsRequired(jobDetails.getSkillsRequired());
        job.setSalaryRange(jobDetails.getSalaryRange());
        job.setEmployer(jobDetails.getEmployer());
        job.setCategory(jobDetails.getCategory());

        return jobRepository.save(job);
    }
    public List<Job> searchJobs(String keyword) {
        return jobRepository.searchJobs(keyword);
    }

    // Delete a job
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id " + id));
        jobRepository.delete(job);
    }
}
