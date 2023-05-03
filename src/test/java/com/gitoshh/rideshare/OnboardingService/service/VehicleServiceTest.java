package com.gitoshh.rideshare.OnboardingService.service;


import com.gitoshh.rideshare.OnboardingService.entity.Vehicle;
import com.gitoshh.rideshare.OnboardingService.repo.VehicleRepository;
import com.gitoshh.rideshare.OnboardingService.request.VehicleCreateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class VehicleServiceTest {
    @InjectMocks
    private VehicleService underTest;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserService userService;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = Vehicle.builder()
                .userId(1L)
                .make("test")
                .model("test")
                .yearOfManufacture(2020)
                .color("test")
                .licensePlate("test")
                .build();
    }

    @AfterEach
    void tearDown() {
        vehicleRepository.deleteAll();
    }


    @Test
    void itFetchesVehicleById() {
        // given
        doReturn(Optional.of(vehicle)).when(vehicleRepository).findById(1L);

        // when
        Vehicle vehicleById = underTest.getVehicleById(1L);
        // then
        assertEquals(vehicleById, vehicle);

    }

    @Test
    void itFetchesVehicleByUserId() {

        // given
        doReturn(Optional.of(vehicle)).when(vehicleRepository).findByUserId(1L);

        // when
        Vehicle vehicleById = underTest.getVehicleByUserId(1L);
        // then
        assertEquals(vehicleById, vehicle);
    }

    @Test
    void itCreatesNewVehicle() {
        // given
        doReturn(true).when(userService).userExists(1L);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        // when
        VehicleCreateRequest vehicleCreateRequest = VehicleCreateRequest.builder()
                .userId(1L)
                .make("test")
                .model("test")
                .year(2020)
                .color("test")
                .licensePlate("test")
                .build();

        // when
        underTest.newVehicle(vehicleCreateRequest);

        // verify
        ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(vehicleCaptor.capture());
        Vehicle newVehicle = vehicleCaptor.getValue();
        assertEquals(vehicleCreateRequest.licensePlate(), newVehicle.getLicensePlate());
    }
}