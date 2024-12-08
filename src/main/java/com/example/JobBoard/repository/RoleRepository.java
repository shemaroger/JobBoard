package com.example.JobBoard.repository;
import com.example.JobBoard.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name); // Find a role by its name (e.g., "EMPLOYER", "JOB_SEEKER")
}
