package com.capstone_design.mobile_forensics.log.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity @Table(name = "wifi")
@Builder @RequiredArgsConstructor
@ToString
public class WifiLog implements LogEntityEntry{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long logId;

    private String SSID;
    private String BSSID;
    private String Latitude;
    private String Longitude;

    private LocalDateTime timestamp;
}
