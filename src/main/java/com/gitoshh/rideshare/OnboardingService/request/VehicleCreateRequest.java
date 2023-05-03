package com.gitoshh.rideshare.OnboardingService.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record VehicleCreateRequest(
        @NotEmpty(message = "Make cannot be empty")
        String make,
        @NotEmpty(message = "Model cannot be empty")
        String model,
        @NotNull(message = "Year cannot be null")
        int year,
        @NotEmpty(message = "Color cannot be empty")
        String color,
        @NotEmpty(message = "License plate cannot be empty")
        String licensePlate,
        @NotEmpty(message = "Insurance policy number cannot be empty")
        String insurancePolicyNumber,
        @NotNull(message = "User ID cannot be null")
        Long userId
) {
}
