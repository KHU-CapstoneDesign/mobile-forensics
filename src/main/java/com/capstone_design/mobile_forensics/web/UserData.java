package com.capstone_design.mobile_forensics.web;

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

    private String latitude;
    private String longitude;
    private LocalDateTime dateTime;
}
