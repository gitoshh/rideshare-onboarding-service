package com.gitoshh.rideshare.OnboardingService.controller;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import com.gitoshh.rideshare.OnboardingService.entity.Vehicle;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import com.gitoshh.rideshare.OnboardingService.repo.VehicleRepository;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
class VehicleControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private HttpHeaders requestHeaders;

    private Vehicle vehicle;

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


        // Create vehicle
        vehicle = Vehicle.builder()
                .userId(user.getId())
                .make("test")
                .model("test")
                .yearOfManufacture(2021)
                .color("test")
                .licensePlate("test")
                .build();
        vehicleRepository.save(vehicle);
    }

    @AfterEach
    void tearDown() {
        vehicleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getVehicleById() throws Exception {
        mvc.perform(get("/api/v1/vehicles/" + vehicle.getId())
                        .headers(requestHeaders)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getVehicleByUserId() throws Exception {
        mvc.perform(get("/api/v1/vehicles/by/user/" + user.getId())
                        .headers(requestHeaders)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createVehicle() throws Exception {
        mvc.perform(post("/api/v1/vehicles")
                        .contentType(APPLICATION_JSON)
                        .content(
                                """
                                        {
                                            "userId": "%s",
                                            "make": "test",
                                            "model": "test",
                                            "year": 2021,
                                            "color": "test",
                                            "licensePlate": "test",
                                            "insurancePolicyNumber": "test"
                                        }
                                        """.replaceFirst("%s", user.getId().toString()))
                        .headers(requestHeaders)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}