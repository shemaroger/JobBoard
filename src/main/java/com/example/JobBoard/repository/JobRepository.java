package com.example.JobBoard.repository;
import com.example.JobBoard.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCategory_Id(Long categoryId); // Find all jobs by category ID
    List<Job> findByEmployer_Id(Long employerId); // Find all jobs posted by a specific employer
    List<Job> findByLocationContaining(String location); // Find jobs by location (partial match)
}
