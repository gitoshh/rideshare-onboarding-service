package com.gitoshh.rideshare.OnboardingService.service;

import com.gitoshh.rideshare.OnboardingService.entity.Role;
import com.gitoshh.rideshare.OnboardingService.entity.User;
import com.gitoshh.rideshare.OnboardingService.contracts.FileStorageInterface;
import com.gitoshh.rideshare.OnboardingService.exception.NotFoundException;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import com.gitoshh.rideshare.OnboardingService.request.UserSignupRequest;
import com.gitoshh.rideshare.OnboardingService.response.UserResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.entity.ContentType.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final FileStorageInterface fileStore;
    private final PasswordEncoder passwordEncoder;


    /**
     * Get user by id
     *
     * @param id - user id
     * @return - user
     */
    public UserResponseEntity getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        return generateUserResponse(user);
    }

    /**
     * Get user by email
     *
     * @param email - user email
     * @return - user
     */
    public UserResponseEntity getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        return generateUserResponse(user);
    }

    /**
     * Save user in DB and upload image to S3
     *
     * @param userSignupRequest - user signup request
     * @return - saved user
     */
    public UserResponseEntity saveUser(UserSignupRequest userSignupRequest) {
        User user = User.builder()
                .email(userSignupRequest.email())
                .password(passwordEncoder.encode(userSignupRequest.password()))
                .phone(userSignupRequest.phone())
                .firstName(userSignupRequest.firstName())
                .lastName(userSignupRequest.lastName())
                .role(userSignupRequest.isRider() ? Role.RIDER : Role.DRIVER)
                .build();

        // For users who sign up as riders, we will set onBoarded to true
        if (userSignupRequest.isRider()) {
            log.info(true);
            user.setOnBoarded(true);
        }

//        MultipartFile file = userSignupRequest.file();
//
//        // Check if the file is empty
//        if (file.isEmpty()) {
//            throw new IllegalStateException("Cannot upload empty file");
//        }
//
//        // Check if the file is an image
//        if (!isFileAnImage(file)) {
//            throw new IllegalStateException("File must be an image");
//        }
//
//        // Save Image in S3 and then save user in DB
//        String avatarUrl = uploadFile(file);
//        user.setAvatar(avatarUrl);

        User newUser = userRepository.save(user);

        return generateUserResponse(newUser);
    }

    /**
     * Upload file to S3
     *
     * @param file - multipart file
     * @return - the file name
     */
    private String uploadFile(MultipartFile file) {
        // Get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        // Save Image in S3
        String fileName = String.format("%s", file.getOriginalFilename());
        try {
            return fileStore.uploadFile(fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
    }

    /**
     * Check if the file is an image
     *
     * @param file - multipart file
     * @return - true if the file is an image
     */
    private boolean isFileAnImage(MultipartFile file) {
        return Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType());
    }

    /**
     * Checks if user with the given id exists
     */
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * Generate user response
     * @param user - user
     * @return - user response
     */
    private UserResponseEntity generateUserResponse(User user) {
        return UserResponseEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .role(user.getRole())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .isOnBoarded(user.isOnBoarded())
                .avatar(user.getAvatar())
                .verificationCode(user.getVerificationCode())

                .build();

    }

    /**
     * Complete onboarding
     * @param id - user id
     * @return - user response
     */
    public UserResponseEntity completeOnBoarding(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        user.setOnBoarded(true);
        return generateUserResponse(user);
    }
}
