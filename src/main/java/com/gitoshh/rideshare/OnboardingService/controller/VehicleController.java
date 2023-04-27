package com.gitoshh.rideshare.OnboardingService.controller;

import com.gitoshh.rideshare.OnboardingService.entity.Vehicle;
import com.gitoshh.rideshare.OnboardingService.request.VehicleCreateRequest;
import com.gitoshh.rideshare.OnboardingService.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
public record VehicleController(VehicleService vehicleService) {

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return new ResponseEntity<>(vehicle, null != vehicle ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/by/user/{userId}")
    public ResponseEntity<Vehicle> getVehicleByUserId(@PathVariable Long userId) {
        Vehicle vehicle = vehicleService.getVehicleByUserId(userId);
        return new ResponseEntity<>(vehicle, null != vehicle ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody VehicleCreateRequest vehicleCreateRequest) {
        return new ResponseEntity<>(vehicleService.newVehicle(vehicleCreateRequest), HttpStatus.CREATED);
    }

}
