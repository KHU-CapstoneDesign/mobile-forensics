package com.capstone_design.mobile_forensics.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "gps_image_metadata")
@Builder @AllArgsConstructor @NoArgsConstructor
@ToString @Getter
public class GPSMetadata implements LogEntityEntry {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long logId;

    private double latitude;
    private double longitude;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

}
