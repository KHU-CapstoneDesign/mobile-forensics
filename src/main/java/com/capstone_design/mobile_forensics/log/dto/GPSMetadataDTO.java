package com.capstone_design.mobile_forensics.log.dto;

import com.capstone_design.mobile_forensics.log.entity.GPSMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class GPSMetadataDTO implements LogEntry {

    private String Latitude;
    private String Longitude;
    private LocalDateTime timestamp;

    @Override
    public String getLogType() {
        return "GPS Metadata of Picture";
    }

    public GPSMetadata toEntity() {
        return GPSMetadata.builder()
                .Latitude(Latitude)
                .Longitude(Longitude)
                .timestamp(timestamp)
                .build();
    }
}
