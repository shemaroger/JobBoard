package com.example.JobBoard.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_categories")
@Data
public class JobCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "Software Development", "Marketing", "Finance"
}
