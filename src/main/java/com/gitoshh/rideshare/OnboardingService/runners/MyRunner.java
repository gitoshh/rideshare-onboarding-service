package com.gitoshh.rideshare.OnboardingService.runners;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import com.gitoshh.rideshare.OnboardingService.entity.Vehicle;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import com.gitoshh.rideshare.OnboardingService.repo.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "environment.name", havingValue = "local")
@RequiredArgsConstructor
public class MyRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final VehicleRepository vehicleRepository;


    @Override

    public void run(String... args) throws Exception {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .password(passwordEncoder.encode("password"))
                .phone("0712345678")
                .role(Role.DRIVER)
                .onBoarded(true)
                .build();
        userRepository.save(user);

        User user1 = User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@gmail.com")
                .password(passwordEncoder.encode("password"))
                .phone("0712345679")
                .onBoarded(true)
                .role(Role.RIDER)
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .firstName("John1")
                .lastName("Doe")
                .email("john1.doe@gmail.com")
                .password(passwordEncoder.encode("password"))
                .phone("0712345670")
                .onBoarded(false)
                .role(Role.DRIVER)
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .firstName("John2")
                .lastName("Doe")
                .email("john2.doe@gmail.com")
                .password(passwordEncoder.encode("password"))
                .phone("0712345671")
                .onBoarded(true)
                .role(Role.ADMIN)
                .build();
        userRepository.save(user3);

        User user4 = User.builder()
                .firstName("Jane2")
                .lastName("Doe")
                .email("jane2.doe@gmail.com")
                .password(passwordEncoder.encode("password"))
                .phone("0712345672")
                .onBoarded(true)
                .role(Role.DRIVER)
                .build();
        userRepository.save(user4);

        vehicleRepository.save(Vehicle.builder()
                .make("Toyota")
                .model("Corolla")
                .yearOfManufacture(2010)
                .color("Black")
                .licensePlate("KCA 123A")
                .userId(user.getId())
                .insurancePolicyNumber("223456789")
                .build());

        vehicleRepository.save(Vehicle.builder()
                .make("Mazda")
                .model("Demio")
                .yearOfManufacture(2010)
                .color("White")
                .licensePlate("KCC 145B")
                .insurancePolicyNumber("123456709")
                .userId(user2.getId())
                .build());

        vehicleRepository.save(Vehicle.builder()
                .make("Mazda")
                .model("Axela")
                .yearOfManufacture(2010)
                .color("White")
                .licensePlate("KDA 145B")
                .insurancePolicyNumber("123456789")
                .userId(user4.getId())
                .build());
    }
}
