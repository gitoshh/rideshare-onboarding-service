package com.gitoshh.rideshare.OnboardingService.request;


import lombok.Builder;

@Builder
public record UserLoginRequest(
        String email,
        String password
) {
}
