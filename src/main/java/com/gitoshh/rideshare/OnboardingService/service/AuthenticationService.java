package com.gitoshh.rideshare.OnboardingService.service;


import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import com.gitoshh.rideshare.OnboardingService.exception.NotFoundException;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import com.gitoshh.rideshare.OnboardingService.request.UserLoginRequest;
import com.gitoshh.rideshare.OnboardingService.response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        // Authenticate user with email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequest.email(), userLoginRequest.password())
        );
        // Fetch user from database
        User user = userRepository.findByEmail(userLoginRequest.email()).orElseThrow(() -> new NotFoundException("User not found"));
        // Generate token
        String jwtToken = "Bearer " + jwtService.generateToken(user);

        // Return response
        return UserLoginResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .role(user.getRole())
                .isOnBoarded(user.isOnBoarded())
                .token(jwtToken)
                .build();
    }
}
