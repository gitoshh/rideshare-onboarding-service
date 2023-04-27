package com.gitoshh.rideshare.OnboardingService.controller;


import com.gitoshh.rideshare.OnboardingService.request.UserLoginRequest;
import com.gitoshh.rideshare.OnboardingService.request.UserSignupRequest;
import com.gitoshh.rideshare.OnboardingService.response.UserLoginResponse;
import com.gitoshh.rideshare.OnboardingService.response.UserResponseEntity;
import com.gitoshh.rideshare.OnboardingService.service.AuthenticationService;
import com.gitoshh.rideshare.OnboardingService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/auth")
public record AuthenticationController(UserService userService, AuthenticationService authenticationService) {
    @PostMapping("/signup")
    public ResponseEntity<UserResponseEntity> signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        return ResponseEntity.ok(userService.saveUser(userSignupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLoginRequest userSignupRequest) {
        return ResponseEntity.ok(authenticationService.login(userSignupRequest));
    }
}
