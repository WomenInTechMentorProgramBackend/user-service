package com.example.userservice.controller;

import com.example.userservice.dto.PhotoResponse;
import com.example.userservice.entity.Photo;
import com.example.userservice.service.PhotoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/v1")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PreAuthorize(value = "hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/doctors/{userId}/photos/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("image")MultipartFile file,
            @PathVariable UUID userId
    ) throws IOException {
        try{
            String uploadPhoto = photoService.uploadPhoto(file, userId);
            return ResponseEntity.status(HttpStatus.OK).body(uploadPhoto);
        } catch (Exception e) {
            String errorMessage = "Could not upload the image: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorMessage);
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) {
        byte[] imageData = photoService.downloadPhoto(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(IMAGE_PNG_VALUE))
                .body(imageData);
    }

    @GetMapping("/photos")
    public ResponseEntity<List<PhotoResponse>> getListPhotos() {
        List<PhotoResponse> photos = photoService.getAllPhotos().map(dbPhoto -> {
            String photoDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/photos")
                    .path(String.valueOf(dbPhoto.getId()))
                    .toUriString();

            return new PhotoResponse(
                    dbPhoto.getName(),
                    photoDownloadUri,
                    dbPhoto.getType(),
                    dbPhoto.getPhotoData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(photos);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id) {
        Photo photo = photoService.getPhoto(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + photo.getName() + "\"")
                .body(photo.getPhotoData());
    }
}