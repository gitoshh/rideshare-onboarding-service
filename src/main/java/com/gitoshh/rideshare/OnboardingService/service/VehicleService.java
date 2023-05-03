package com.gitoshh.rideshare.OnboardingService.service;

import com.gitoshh.rideshare.OnboardingService.entity.Vehicle;
import com.gitoshh.rideshare.OnboardingService.exception.NotFoundException;
import com.gitoshh.rideshare.OnboardingService.repo.VehicleRepository;
import com.gitoshh.rideshare.OnboardingService.request.VehicleCreateRequest;
import org.springframework.stereotype.Service;

@Service
public record VehicleService(VehicleRepository vehicleRepository, UserService userService) {

    /**
     * Get vehicle by id
     * @param id - vehicle id
     * @return  - vehicle
     */
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElseThrow(()->
                new NotFoundException("Vehicle not found"));
    }

    /**
     * Get vehicle by user id
     * @param userId - user id
     * @return - vehicle
     */
    public Vehicle getVehicleByUserId(Long userId) {
        return vehicleRepository.findByUserId(userId).orElseThrow(()->
                new NotFoundException("Vehicle not found"));
    }

    /**
     * Create new vehicle
     * @param vehicle - vehicle create request
     * @return - vehicle
     */
    public Vehicle newVehicle(VehicleCreateRequest vehicle) {
        if(!userService.userExists(vehicle.userId())) {
            throw new NotFoundException("User not found");
        }
        Vehicle newVehicle = Vehicle.builder()
                .make(vehicle.make())
                .model(vehicle.model())
                .yearOfManufacture(vehicle.year())
                .color(vehicle.color())
                .licensePlate(vehicle.licensePlate())
                .insurancePolicyNumber(vehicle.insurancePolicyNumber())
                .userId(vehicle.userId())
                .build();

        return vehicleRepository.save(newVehicle);
    }
}
