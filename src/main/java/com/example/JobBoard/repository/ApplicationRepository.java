package com.example.JobBoard.repository;
import com.example.JobBoard.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJob_Id(Long jobId); // Find all applications for a specific job

}
