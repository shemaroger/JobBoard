package com.example.JobBoard.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // Additional Details for Job Seekers
    private String headline; // e.g., "Software Developer with 5 Years of Experience"
    private String summary; // A detailed description about the job seeker
    private String skills; // e.g., "Java, React, Spring Boot"
    private String linkedinProfile;
    private String githubProfile;
    private String resumeUrl; // URL to uploaded resume

    // Additional Details for Employers
    private String companyName; // Relevant only for employers
    private String companyWebsite;
}
