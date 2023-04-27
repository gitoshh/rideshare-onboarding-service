package com.gitoshh.rideshare.OnboardingService.repo;


import com.gitoshh.rideshare.OnboardingService.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v FROM Vehicle v WHERE v.userId = ?1")
    Optional<Vehicle> findByUserId(Long userId);
}
