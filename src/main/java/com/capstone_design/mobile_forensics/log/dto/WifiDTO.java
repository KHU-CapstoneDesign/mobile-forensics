package com.capstone_design.mobile_forensics.log.dto;

import com.capstone_design.mobile_forensics.log.entity.WifiLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WifiDTO implements LogEntry {
    private String ssid;
    private String bssid;
    private String latitude;
    private String longitude;
    private LocalDateTime timestamp;

    @Override
    public String getLogType() {
        return "WiFi Log";
    }

    public WifiLog toEntity() {
        return WifiLog.builder()
                .ssid(ssid)
                .bssid(bssid)
                .latitude(latitude)
                .longitude(longitude)
                .timestamp(timestamp)
                .build();

    }
}
