package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Otp;
import com.blogsphere.blogsphere.model.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(String email, OtpPurpose purpose);
    Optional<Otp> findTopByEmailAndPurposeOrderByCreatedAtDesc(String email, OtpPurpose purpose);
    void deleteByEmailAndPurpose(String email, OtpPurpose purpose);
    void deleteByUsedTrueOrExpiresAtBefore(LocalDateTime cutoff);
}
