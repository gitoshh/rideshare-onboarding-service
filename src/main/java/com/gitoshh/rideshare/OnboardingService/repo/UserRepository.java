package com.gitoshh.rideshare.OnboardingService.repo;

import com.gitoshh.rideshare.OnboardingService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    java.util.Optional<User> findByEmail(String email);
}
