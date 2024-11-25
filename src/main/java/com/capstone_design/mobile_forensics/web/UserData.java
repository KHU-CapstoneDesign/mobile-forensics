package com.capstone_design.mobile_forensics.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class UserData {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    private double latitude;
    private double longitude;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
}
