package com.capstone_design.mobile_forensics.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "picture_taken_log")
@Builder @AllArgsConstructor @NoArgsConstructor
@ToString @Getter
public class TakenPictureLog implements LogEntityEntry {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long logId;

    private String tag;
    private String eventType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
