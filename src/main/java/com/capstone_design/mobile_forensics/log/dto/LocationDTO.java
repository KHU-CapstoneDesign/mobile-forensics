package com.capstone_design.mobile_forensics.log.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data @AllArgsConstructor @NoArgsConstructor
public class LocationDTO {
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;
}
