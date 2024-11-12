package com.capstone_design.mobile_forensics.log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Builder @RequiredArgsConstructor
@ToString
public class AppUsageLog implements LogEntityEntry {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long logId;

    private String type;

    private String packageName;
    private String className;

    private LocalDateTime timestamp;
}
