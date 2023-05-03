package com.gitoshh.rideshare.OnboardingService.service;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import com.gitoshh.rideshare.OnboardingService.request.UserLoginRequest;
import com.gitoshh.rideshare.OnboardingService.response.UserLoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationService underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user = User.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode("testPassword"))
                .firstName("test")
                .lastName("test")
                .phone("test")
                .role(Role.DRIVER)
                .onBoarded(true)
                .avatar(null)
                .isVerified(false)
                .verificationCode(null)
                .build();
    }

    @Test
    void login() {
        doReturn(Optional.of(user)).when(userRepository).findByEmail(user.getEmail());

        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        UserLoginResponse userLoginResponse = underTest.login(userLoginRequest);

        assertThat(userLoginResponse.token()).contains("Bearer");
    }
}