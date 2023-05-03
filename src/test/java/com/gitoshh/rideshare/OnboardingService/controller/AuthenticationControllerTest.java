package com.gitoshh.rideshare.OnboardingService.controller;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void itAllowsForSignup() throws Exception {
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                 {
                                 "email": "test@mail.com",
                                  "password": "testPassword",
                                  "firstName": "firstNameTest",
                                  "lastName": "lastNameTest",
                                  "phone": "1234567890",
                                  "isRider": true,
                                  "isDriver": false
                                 }
                                """)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void itValidatesForSignup() throws Exception {
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                 {
                                 "email": "test@mail.com",
                                  "password": "testPassword",
                                  "firstName": "firstNameTest",
                                  "lastName": "lastNameTest",
                                  "phone": "",
                                  "isRider": true,
                                  "isDriver": false
                                 }
                                """).accept(APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void itReturnUnAuthorisedForUnavailableUsers() throws Exception {
        mvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                 {
                                 "email": "test@mail.com",
                                  "password": "testPassword"
                                 }
                                """).accept(APPLICATION_JSON))
                .andExpect(status().is(401));
    }

    @Test
    void itReturnOkForAvailableUsersWithTheRightDetails() throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode("testPassword"))
                .firstName("test")
                .lastName("test")
                .phone("test")
                .role(Role.DRIVER)
                .build();

        userRepository.save(user);
        mvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                 {
                                 "email": "test@gmail.com",
                                  "password": "testPassword"
                                 }
                                """).accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}