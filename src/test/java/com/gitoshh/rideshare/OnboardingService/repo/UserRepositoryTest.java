package com.gitoshh.rideshare.OnboardingService.repo;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindUserByEmail() {
        // given
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .firstName("test")
                .lastName("test")
                .phone("test")
                .role(Role.RIDER)
                .onBoarded(false)
                .avatar("")
                .isVerified(false)
                .verificationCode("")
                .build();

        underTest.save(user);

        // when
        boolean exists = underTest.findByEmail("test@gmail.com").isPresent();
        // then
        assertTrue(exists);

        // when
        boolean unusedEmailExists = underTest.findByEmail("jane@gmail.com").isPresent();
        assertFalse(unusedEmailExists);
    }
}