package com.capstone_design.mobile_forensics.log.entity;

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

    private LocalDateTime timestamp;

}
