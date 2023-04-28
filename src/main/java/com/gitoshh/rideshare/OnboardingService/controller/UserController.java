package com.gitoshh.rideshare.OnboardingService.controller;

import com.gitoshh.rideshare.OnboardingService.request.UserByEmailRequest;
import com.gitoshh.rideshare.OnboardingService.response.UserResponseEntity;
import com.gitoshh.rideshare.OnboardingService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public record UserController(UserService userService) {
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseEntity> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/by/email")
    public ResponseEntity<UserResponseEntity> getUserByEmail(@Valid @RequestBody UserByEmailRequest userByEmailRequest) {
        return ResponseEntity.ok(userService.getUserByEmail(userByEmailRequest.email()));
    }

    @PostMapping("/{id}/complete/onboarding")
    public ResponseEntity<UserResponseEntity> completeOnBoarding(@PathVariable Long id) {
        return ResponseEntity.ok(userService.completeOnBoarding(id));
    }

}
