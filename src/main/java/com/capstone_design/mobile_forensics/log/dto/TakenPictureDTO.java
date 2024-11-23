package com.capstone_design.mobile_forensics.log.dto;

import com.capstone_design.mobile_forensics.log.entity.TakenPictureLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakenPictureDTO implements LogEntry {
    private LocalDateTime timestamp;
    private String tag;
    private String eventType;

    @Override
    public String getLogType() {
        return "Taken Picture Log";
    }

    @Override
    public String toString() {
        return timestamp + " | " + tag + " | " + eventType;
    }

    public TakenPictureLog toEntity() {
        return TakenPictureLog.builder()
                .tag(tag)
                .eventType(eventType)
                .timestamp(timestamp)
                .build();
    }
}