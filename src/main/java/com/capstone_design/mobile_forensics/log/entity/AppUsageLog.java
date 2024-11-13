package com.capstone_design.mobile_forensics.log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder @AllArgsConstructor @NoArgsConstructor
@ToString
public class AppUsageLog implements LogEntityEntry {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long logId;

    private String type;

    private String packageName;
    private String className;

    private LocalDateTime timestamp;
}
