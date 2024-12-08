package com.example.JobBoard.repository;
import com.example.JobBoard.model.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {
    JobCategory findByName(String name); // Find a category by its name
}
