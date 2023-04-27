package com.gitoshh.rideshare.OnboardingService.request;

public record UserLoginRequest(
        String email,
        String password
) {
}
