package com.example.JobBoard.service;
import com.example.JobBoard.model.Application;
import com.example.JobBoard.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJob_Id(jobId);
    }

    public List<Application> getApplicationsByJobSeeker(Long jobSeekerId) {
        return applicationRepository.findByJobSeeker_Id(jobSeekerId);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }
}
