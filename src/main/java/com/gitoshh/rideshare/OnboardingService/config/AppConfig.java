package com.gitoshh.rideshare.OnboardingService.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.gitoshh.rideshare.OnboardingService.contracts.FileStorageInterface;
import com.gitoshh.rideshare.OnboardingService.exception.NotFoundException;
import com.gitoshh.rideshare.OnboardingService.repo.UserRepository;
import com.gitoshh.rideshare.OnboardingService.service.external.AWSFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final UserRepository userRepository;

    @Value("${aws.access_key}")
    private String ACCESS_KEY;
    @Value("${aws.secret_key}")
    private String SECRET_KEY;
    @Value("${aws.region}")
    private String REGION;

    @Value("${server.allowed_origins}")
    private String allowedOrigins;

    public AWSCredentials credentials() {
        return new BasicAWSCredentials(
                ACCESS_KEY,
                SECRET_KEY
        );
    }

    /**
     * Bean for AmazonS3
     * @return AmazonS3
     */
    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.fromName(REGION))
                .build();
    }

    /**
     * Bean for FileStorageInterface
     * @return FileStorageInterface
     */
    @Bean
    FileStorageInterface fileStorageInterface() {
        return new AWSFileStorageService(amazonS3());
    }

    /**
     * Bean for UserDetailsService
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return (username -> userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("User not found")));
    }

    /**
     * Bean for AuthenticationProvider
     * @return AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Bean for PasswordEncoder
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean for AuthenticationManager
     * @param configuration AuthenticationConfiguration
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();
        String[] allowedOriginsArrays = allowedOrigins.split(",");

        configuration.setAllowCredentials(true);
        for (String allowedOrigin: allowedOriginsArrays) {
            configuration.addAllowedOrigin(allowedOrigin);
        }
        configuration.addAllowedHeader("*");

        String[] allowedMethods = {"OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE", "PATCH"};
        for (String allowedMethod: allowedMethods) {
            configuration.addAllowedMethod(allowedMethod);
        }
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

}
