package com.example.JobBoard.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String employmentType; // e.g., "Full-Time", "Part-Time", "Contract"

    @Column(nullable = false)
    private String skillsRequired;

    private String salaryRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer; // Reference to the employer who posted the job

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private JobCategory category; // Reference to the job category
}
