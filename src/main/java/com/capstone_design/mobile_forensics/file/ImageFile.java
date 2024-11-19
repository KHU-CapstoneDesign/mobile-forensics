package com.capstone_design.mobile_forensics.file;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.CookieValue;

import java.time.LocalDateTime;

@Entity
@Getter @ToString(exclude = "imageByte")
@AllArgsConstructor @NoArgsConstructor
public class ImageFile {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long imageId;
    private String imageName;
    @Column(length = 100000)
    private byte[] imageByte;
    private String type; //camera, trash, soda, drive
    private LocalDateTime timestamp;
}
