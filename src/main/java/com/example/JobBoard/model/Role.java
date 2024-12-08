package com.example.JobBoard.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "EMPLOYER", "JOB_SEEKER", "ADMIN"
}
