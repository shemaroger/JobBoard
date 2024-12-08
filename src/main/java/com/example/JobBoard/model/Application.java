package com.example.JobBoard.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications")
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job; // Reference to the job being applied for

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_seeker_id", nullable = false)
    private User jobSeeker; // Reference to the user applying

    @Column(nullable = false)
    private String status; // e.g., "Pending", "Accepted", "Rejected"

    private String resumeUrl; // Optional if the user uploads a specific resume for this application
}
