package com.example.JobBoard.repository;

import com.example.JobBoard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Find a user by email

    @Query("SELECT u FROM User u " +
            "WHERE u.name LIKE %:keyword% " +
            "OR u.email LIKE %:keyword% " +
            "OR u.role.name LIKE %:keyword%")
    List<User> searchUser(@Param("keyword") String keyword);

}
