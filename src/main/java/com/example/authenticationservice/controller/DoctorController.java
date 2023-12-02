package com.example.authenticationservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {

    @GetMapping
    public String get() {
        return "GET:: doctor controller";
    }

    @PostMapping
    public String post() {
        return "post:: doctor controller";
    }

    @PutMapping
    public String Put() {
        return "Put:: doctor controller";
    }

    @DeleteMapping
    public String Delete() {
        return "Delete:: doctor controller";
    }
}
