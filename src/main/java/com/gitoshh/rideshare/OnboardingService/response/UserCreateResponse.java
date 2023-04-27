package com.gitoshh.rideshare.OnboardingService.response;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import lombok.Builder;

@Builder
public record UserCreateResponse(
        Long id,
        String email,
        String phone,
        String firstName,
        String lastName,
        Role role
        ){
}
