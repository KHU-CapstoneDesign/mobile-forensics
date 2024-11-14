package com.capstone_design.mobile_forensics.log.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class GeolocationRequest {
    private String macAddress; //BSSID
    private double signalStrength;
    private Integer age; //durationMillis
    private Integer channel; //mChannelInfo
}