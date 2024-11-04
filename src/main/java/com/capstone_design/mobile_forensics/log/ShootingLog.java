package com.capstone_design.mobile_forensics.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShootingLog implements LogEntry {
    private LocalDateTime timestamp;
    private String tag;
    private String eventType;

    @Override
    public String getLogType() {
        return "Camera Shooting Log";
    }

    @Override
    public String toString() {
        return timestamp + " | " + tag + " | " + eventType;
    }
}