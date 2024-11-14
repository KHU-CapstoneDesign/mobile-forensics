package com.capstone_design.mobile_forensics.log.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class GeolocationResponse {
    private Location location;
    private double accuracy;
    @Data
    public static class Location {
        private double lat;
        private double lng;
    }
}
