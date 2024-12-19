package com.example.JobBoard.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job; // Reference to the job being applied for

    @Column(nullable = false)
    private String fullName; // Full name of the applicant

    @Column(nullable = false)
    private String email; // Email of the applicant

    @Column(nullable = false)
    private String phoneNumber; // Phone number of the applicant

    @Column(nullable = false)
    private String status; // e.g., "Pending", "Accepted", "Rejected"

    @ElementCollection
    @CollectionTable(name = "application_files", joinColumns = @JoinColumn(name = "application_id"))
    @Column(name = "file_url")
    private List<String> fileUrls; // Stores multiple file URLs or paths

    // Getter and Setter for 'id'
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for 'job'
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    // Getter and Setter for 'fullName'
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for 'email'
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for 'phoneNumber'
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter and Setter for 'status'
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter and Setter for 'fileUrls'
    public List<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;
    }
}
