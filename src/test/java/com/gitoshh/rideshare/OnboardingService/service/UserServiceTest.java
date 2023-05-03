package com.gitoshh.rideshare.OnboardingService.service;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import com.gitoshh.rideshare.OnboardingService.request.UserSignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;


    @BeforeEach
    void setUp() {
        doReturn("testPassword").when(passwordEncoder).encode("testPassword");
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
    void itGetsUserById() {

        // given
        Optional<User> optionalUser = Optional.of(user);
        doReturn(optionalUser).when(userRepository).findById(1L);

        // when
        userService.getUserById(1L);
        // verify
        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).findById(userIdCaptor.capture());
        Long userId = userIdCaptor.getValue();
        assertEquals(1L, userId);
    }

    @Test
    void itFetchesUserByEmail() {
        // given
        Optional<User> optionalUser = Optional.of(user);
        doReturn(optionalUser).when(userRepository).findByEmail(user.getEmail());

        // when
        userService.getUserByEmail(user.getEmail());

        // verify
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail(emailCaptor.capture());
        String email = emailCaptor.getValue();
        assertEquals(user.getEmail(), email);
    }

    @Test
    void itSaveNewUser() {
        // given
        doReturn("testPassword").when(passwordEncoder).encode("testPassword");
        UserSignupRequest userSignupRequest = UserSignupRequest.builder()
                .email(user.getEmail())
                .password("testPassword")
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .isRider(true)
                .isDriver(false)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        // when
        userService.saveUser(userSignupRequest);

        // verify
        ArgumentCaptor<User> emailCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(emailCaptor.capture());
        User newUser = emailCaptor.getValue();
        assertEquals(userSignupRequest.email(), newUser.getEmail());
    }

    @Test
    void itReturnsTrueIfUserExistsAndFalseIfUserDoesNot() {
        // given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(false);


        // when
        boolean exists = userService.userExists(1L);
        boolean existsNot = userService.userExists(2L);

        // then
        assertFalse(existsNot);
        assertTrue(exists);
    }
}