package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {
    Optional<PendingRegistration> findByEmail(String email);
    void deleteByEmail(String email);
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
