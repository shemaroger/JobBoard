package com.example.JobBoard.controller;

import com.example.JobBoard.model.JobCategory;
import com.example.JobBoard.service.JobCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;

    @Autowired
    public JobCategoryController(JobCategoryService jobCategoryService) {
        this.jobCategoryService = jobCategoryService;
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<JobCategory> createCategory(@RequestBody JobCategory category) {
        return ResponseEntity.ok(jobCategoryService.createCategory(category));
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<JobCategory>> getAllCategories() {
        return ResponseEntity.ok(jobCategoryService.getAllCategories());
    }

    // Get category by name
    @GetMapping("/name/{name}")
    public ResponseEntity<JobCategory> getCategoryByName(@PathVariable String name) {
        JobCategory category = jobCategoryService.getCategoryByName(name);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobCategory> getCategoryById(@PathVariable Long id) {
        return jobCategoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update category by ID
    @PutMapping("/{id}")
    public ResponseEntity<JobCategory> updateCategory(@PathVariable Long id, @RequestBody JobCategory category) {
        JobCategory updatedCategory = jobCategoryService.updateCategory(id, category);
        if (updatedCategory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCategory);
    }

    // Delete category by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        boolean isDeleted = jobCategoryService.deleteCategory(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
