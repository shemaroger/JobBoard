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

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public List<Job> getJobsByCategory(Long categoryId) {
        return jobRepository.findByCategory_Id(categoryId);
    }

    public List<Job> getJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployer_Id(employerId);
    }

    public List<Job> getJobsByLocation(String location) {
        return jobRepository.findByLocationContaining(location);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}
