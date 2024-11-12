package com.capstone_design.mobile_forensics.log.repository;


import com.capstone_design.mobile_forensics.log.entity.WifiLog;

import java.time.LocalDateTime;
import java.util.List;

public interface WifiRepository extends LogRepository{
    List<WifiLog> findAllByTimestamp(LocalDateTime timestamp);
    List<WifiLog> findAllByLatitudeAndLongitude(String Latitude, String Longitude);
}
