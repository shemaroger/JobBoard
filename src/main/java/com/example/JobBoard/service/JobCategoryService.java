package com.example.JobBoard.service;
import com.example.JobBoard.model.JobCategory;
import com.example.JobBoard.repository.JobCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    @Autowired
    public JobCategoryService(JobCategoryRepository jobCategoryRepository) {
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public JobCategory createCategory(JobCategory category) {
        return jobCategoryRepository.save(category);
    }

    public List<JobCategory> getAllCategories() {
        return jobCategoryRepository.findAll();
    }

    public JobCategory getCategoryByName(String name) {
        return jobCategoryRepository.findByName(name);
    }
}
