package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.Otp;
import com.blogsphere.blogsphere.model.OtpPurpose;
import com.blogsphere.blogsphere.repository.OtpRepository;
import com.blogsphere.blogsphere.repository.PendingRegistrationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class OtpService {

    private static final int CODE_LENGTH = 6;
    private static final long EXPIRY_MINUTES = 10;
    private static final long PENDING_REGISTRATION_MAX_AGE_HOURS = 24;
    private static final long RESEND_COOLDOWN_SECONDS = 60;

    private final OtpRepository otpRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final EmailService emailService;
    private final SecureRandom random = new SecureRandom();

    public OtpService(OtpRepository otpRepository, PendingRegistrationRepository pendingRegistrationRepository,
                      EmailService emailService) {
        this.otpRepository = otpRepository;
        this.pendingRegistrationRepository = pendingRegistrationRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void generateAndSendOtp(String email, OtpPurpose purpose, String purposeLabel) {
        otpRepository.findTopByEmailAndPurposeOrderByCreatedAtDesc(email, purpose).ifPresent(lastOtp -> {
            LocalDateTime nextAllowedAt = lastOtp.getCreatedAt().plusSeconds(RESEND_COOLDOWN_SECONDS);
            if (LocalDateTime.now().isBefore(nextAllowedAt)) {
                long secondsRemaining = Duration.between(LocalDateTime.now(), nextAllowedAt).getSeconds() + 1;
                throw new IllegalStateException("Please wait " + secondsRemaining + " seconds before requesting another code");
            }
        });

        // Invalidate any previous unused codes for this email/purpose before issuing a new one.
        otpRepository.deleteByEmailAndPurpose(email, purpose);

        String code = generateCode();

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setPurpose(purpose);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES));
        otpRepository.save(otp);

        emailService.sendOtpEmail(email, code, purposeLabel);
    }

    @Transactional
    public void verifyOtp(String email, String code, OtpPurpose purpose) {
        Otp otp = otpRepository.findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(email, purpose)
                .orElseThrow(() -> new ResourceNotFoundException("No pending verification code for this email"));

        if (otp.isUsed()) {
            throw new IllegalStateException("This code has already been used");
        }
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("This code has expired, please request a new one");
        }
        if (!otp.getCode().equals(code)) {
            throw new IllegalArgumentException("Invalid code");
        }

        otp.setUsed(true);
        otpRepository.save(otp);
    }

    private String generateCode() {
        int number = 100000 + random.nextInt(900000); // 6-digit code, always leading digit non-zero
        return String.valueOf(number);
    }

    /**
     * Runs every 15 minutes. Removes:
     *  - OTP rows that are used or past their expiry (no longer redeemable, no reason to keep them)
     *  - PendingRegistration rows older than 24h whose OTP was never verified (abandoned signups)
     */
    @Scheduled(fixedRate = 15 * 60 * 1000)
    @Transactional
    public void cleanupExpiredOtpsAndStaleRegistrations() {
        otpRepository.deleteByUsedTrueOrExpiresAtBefore(LocalDateTime.now());

        LocalDateTime pendingCutoff = LocalDateTime.now().minusHours(PENDING_REGISTRATION_MAX_AGE_HOURS);
        pendingRegistrationRepository.deleteByCreatedAtBefore(pendingCutoff);
    }
}
