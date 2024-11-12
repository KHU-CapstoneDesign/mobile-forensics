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
    private String SSID;
    private String BSSID;
    private String Latitude;
    private String Longitude;
    private LocalDateTime timestamp;

    @Override
    public String getLogType() {
        return "WiFi Log";
    }

    public WifiLog toEntity() {
        return WifiLog.builder()
                .SSID(SSID)
                .BSSID(BSSID)
                .Latitude(Latitude)
                .Longitude(Longitude)
                .timestamp(timestamp)
                .build();

    }
}
