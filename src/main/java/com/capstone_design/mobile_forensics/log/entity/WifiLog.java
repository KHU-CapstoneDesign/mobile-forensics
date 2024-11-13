package com.capstone_design.mobile_forensics.log.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name = "wifi")
@Builder @AllArgsConstructor @NoArgsConstructor
@ToString
public class WifiLog implements LogEntityEntry{

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long logId;

    private String ssid;
    private String bssid;
    private String latitude;
    private String longitude;

    private LocalDateTime timestamp;
}
