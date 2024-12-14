package com.example.JobBoard.repository;

import com.example.JobBoard.model.TwoFactorToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwoFactorTokenRepository extends JpaRepository<TwoFactorToken, Long> {
    Optional<TwoFactorToken> findByToken(String token);
    void deleteByUserId(Long userId);
}
