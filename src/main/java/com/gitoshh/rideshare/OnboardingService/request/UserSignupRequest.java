package com.gitoshh.rideshare.OnboardingService.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UserSignupRequest(
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        String password,
        @NotEmpty
        String phone,
        @NotEmpty
        String firstName,
        @NotEmpty
        String lastName,
        @NotNull
        boolean isDriver,
        @NotNull
        boolean isRider
) {
}
