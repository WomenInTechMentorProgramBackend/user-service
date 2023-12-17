package com.example.authenticationservice;

import com.example.authenticationservice.dto.RegisterRequest;
import com.example.authenticationservice.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.example.authenticationservice.entity.Role.ADMIN;
import static com.example.authenticationservice.entity.Role.DOCTOR;

@SpringBootApplication
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService authenticationService
    ) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@mail.com")
                    .password("password")
                    .role(ADMIN)
                    .build();
            System.out.println(authenticationService.register(admin));

            var doctor = RegisterRequest.builder()
                    .firstName("Doctor")
                    .lastName("Doctor")
                    .email("doctor@mail.com")
                    .password("password")
                    .role(DOCTOR)
                    .build();
            System.out.println(authenticationService.register(doctor));
        };
    }
}
