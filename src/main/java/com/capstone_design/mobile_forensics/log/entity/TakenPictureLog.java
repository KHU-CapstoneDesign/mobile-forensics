package com.capstone_design.mobile_forensics.log.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity @Table(name = "picture_taken_log")
@Builder @RequiredArgsConstructor
@ToString
public class TakenPictureLog implements LogEntityEntry {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long logId;

    private String tag;
    private String eventType;

    private LocalDateTime timestamp;
}
