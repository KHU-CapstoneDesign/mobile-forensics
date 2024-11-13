package com.capstone_design.mobile_forensics.log.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "picture_taken_log")
@Builder @AllArgsConstructor @NoArgsConstructor
@ToString
public class TakenPictureLog implements LogEntityEntry {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long logId;

    private String tag;
    private String eventType;

    private LocalDateTime timestamp;
}
