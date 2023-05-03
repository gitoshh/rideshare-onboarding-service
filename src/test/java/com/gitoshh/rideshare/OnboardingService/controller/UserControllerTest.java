package com.gitoshh.rideshare.OnboardingService.controller;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private HttpHeaders requestHeaders;

    private User user;

    String login() throws Exception {
        String response = mvc.perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                 {
                                    "email": "test@gmail.com",
                                    "password": "testPassword"
                                 }
                                """).accept(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Object obj = JSONValue.parse(response);
        return ((JSONObject) obj).get("token").toString();
    }

    @BeforeEach
    void setUp() throws Exception {
        // Create user
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user = User.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode("testPassword"))
                .firstName("test")
                .lastName("test")
                .phone("test")
                .role(Role.DRIVER)
                .build();
        userRepository.save(user);
        requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
        requestHeaders.add(HttpHeaders.AUTHORIZATION, login());
    }


    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void getUserById() throws Exception {
        mvc.perform(get("/api/v1/users/" + user.getId())
                        .headers(requestHeaders)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserByEmail() throws Exception {
        mvc.perform(post("/api/v1/users/by/email")
                        .content("""
                                {
                                   "email": "test@gmail.com"
                                 }
                                 """)
                        .headers(requestHeaders)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void completeOnBoarding() throws Exception {
        mvc.perform(post("/api/v1/users/" + user.getId() + "/complete/onboarding")
                        .headers(requestHeaders)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}