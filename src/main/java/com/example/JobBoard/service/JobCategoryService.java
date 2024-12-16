package com.example.JobBoard.service;

import com.example.JobBoard.model.JobCategory;
import com.example.JobBoard.repository.JobCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<JobCategory> getCategoryById(Long id) {
        return jobCategoryRepository.findById(id);
    }

    public JobCategory updateCategory(Long id, JobCategory category) {
        Optional<JobCategory> existingCategory = jobCategoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            JobCategory categoryToUpdate = existingCategory.get();
            categoryToUpdate.setName(category.getName());
            return jobCategoryRepository.save(categoryToUpdate);
        }
        return null;
    }

    public boolean deleteCategory(Long id) {
        Optional<JobCategory> existingCategory = jobCategoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            jobCategoryRepository.delete(existingCategory.get());
            return true;
        }
        return false;
    }
}
