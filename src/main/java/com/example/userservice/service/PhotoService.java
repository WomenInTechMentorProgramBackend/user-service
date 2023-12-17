package com.example.userservice.service;

import com.example.userservice.entity.Photo;
import com.example.userservice.entity.User;
import com.example.userservice.repository.PhotoRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.ImageUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    public String uploadPhoto (MultipartFile imageFile, UUID userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        var imageToSave = Photo.builder()
                .name(imageFile.getOriginalFilename())
                .type(imageFile.getContentType())
                .photoData(ImageUtils.compressImage(imageFile.getBytes()))
                .user(user)// Привязка к пользователю
                .build();

        photoRepository.save(imageToSave);

        // Дополнительный код, если необходимо обновить информацию о фото в объекте пользователя
        user.setPhoto(imageToSave);
        userRepository.save(user);

        return "File uploaded successfully: " + imageFile.getOriginalFilename();
    }

    public Photo getPhoto(UUID id) {
        return photoRepository.findById(id).get();
    }

    public Stream<Photo> getAllPhotos() {
        return photoRepository.findAll().stream();
    }

    public byte[] downloadPhoto(String imageName) {
        Optional<Photo> dbImage = photoRepository.findByName(imageName);

        return dbImage.map(image -> {
            try {
                return ImageUtils.decompressImage(image.getPhotoData());
            } catch (DataFormatException | IOException exception) {
                throw new ContextedRuntimeException("Error downloading an image", exception)
                        .addContextValue("Image ID",  image.getId())
                        .addContextValue("Image name", imageName);
            }
        }).orElse(null);
    }
}
