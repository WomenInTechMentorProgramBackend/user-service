package com.example.userservice.dto;

import com.example.userservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private Date birthDate;
    private String email;
    private String phone;
    private String login;
    private String password;
    private Role role;
}
