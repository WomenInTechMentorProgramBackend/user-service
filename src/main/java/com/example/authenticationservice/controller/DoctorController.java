package com.example.authenticationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctor")
@Tag(name = "Doctor")
public class DoctorController {

    @Operation(
            description = "Get endpoint for the doctor",
            summary = "This is a summary for doctor get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
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
