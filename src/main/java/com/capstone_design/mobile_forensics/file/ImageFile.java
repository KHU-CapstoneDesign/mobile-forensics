package com.capstone_design.mobile_forensics.file;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @ToString
@AllArgsConstructor @NoArgsConstructor
public class ImageFile {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imageId;
    private String imageName;
    private byte[] imageByte;
    private LocalDateTime timestamp;
}
