package com.gitoshh.rideshare.OnboardingService.response;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import lombok.Builder;

@Builder
public record UserResponseEntity(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Role role,
        String avatar,
        String verificationCode,
        boolean isOnBoarded
) {
}
