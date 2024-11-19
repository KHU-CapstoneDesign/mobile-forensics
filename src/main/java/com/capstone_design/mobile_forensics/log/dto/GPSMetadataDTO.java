package com.capstone_design.mobile_forensics.log.dto;

import com.capstone_design.mobile_forensics.log.entity.GPSMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class GPSMetadataDTO implements LogEntry {
    /*
    * <latitude - 위도> 37.123456 N
    * <longitude - 경도> 127.123456 E
    * */
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;

    @Override
    public String getLogType() {
        return "GPS Metadata of Picture";
    }

    public GPSMetadata toEntity() {
        return GPSMetadata.builder()
                .latitude(latitude)
                .longitude(longitude)
                .timestamp(timestamp)
                .build();
    }
}
