package com.blogsphere.blogsphere.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDeletionConfirmRequest {

    @NotBlank(message = "otp is required")
    private String otp;
}
