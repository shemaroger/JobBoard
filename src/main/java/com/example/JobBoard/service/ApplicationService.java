package com.example.JobBoard.service;

import com.example.JobBoard.model.Application;
import com.example.JobBoard.model.Job;
import com.example.JobBoard.repository.ApplicationRepository;
import com.example.JobBoard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    public Application saveApplication(Application application, Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        application.setJob(job); // Associate the application with the job
        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJob_Id(jobId);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }
}
