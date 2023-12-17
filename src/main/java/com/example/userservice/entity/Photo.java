package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private String name;

    private String type;

    @Lob
    @JdbcTypeCode(Types.LONGVARBINARY)
    @Column(name = "photodata", length = 20971520)
    private byte[] photoData;

    @OneToOne(mappedBy = "photo")
    private User user;

    public Photo(String fileName, String contentType, byte[] bytes) {
    }
}
