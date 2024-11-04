package com.capstone_design.mobile_forensics.web;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @ToString
@AllArgsConstructor @NoArgsConstructor
public class UserData {

    @Id()
    private Long userId;

    private String Latitude;
    private String Longitude;
    private LocalDateTime dateTime;
}
