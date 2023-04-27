package com.gitoshh.rideshare.OnboardingService.response;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import lombok.Builder;

import java.util.Date;

@Builder
public record UserLoginResponse(
        String token,
        String firstName,
        Long userId,
        Role role
) {
}
