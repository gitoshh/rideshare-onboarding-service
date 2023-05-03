package com.gitoshh.rideshare.OnboardingService.repo;


import com.gitoshh.rideshare.OnboardingService.entity.Vehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VehicleRepositoryTest {

    @Autowired
    private VehicleRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindVehicleByUserId() {
        Vehicle vehicle = Vehicle.builder()
                .userId(1L)
                .make("test")
                .model("test")
                .yearOfManufacture(2020)
                .color("test")
                .licensePlate("test")
                .build();
        // given
        underTest.save(vehicle);

        // when
        boolean exists = underTest.findByUserId(1L).isPresent();
        // then
        assertTrue(exists);

        // when
        boolean unsavedVehicleExists = underTest.findByUserId(2L).isPresent();
        assertFalse(unsavedVehicleExists);
    }
}