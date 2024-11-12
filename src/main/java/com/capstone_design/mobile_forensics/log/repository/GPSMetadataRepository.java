package com.capstone_design.mobile_forensics.log.repository;

import com.capstone_design.mobile_forensics.log.entity.GPSMetadata;

import java.time.LocalDateTime;
import java.util.List;

public interface GPSMetadataRepository extends LogRepository{
    List<GPSMetadata> findAllByTimestamp(LocalDateTime timestamp);
    List<GPSMetadata> findAllByLatitudeAndLongitude(String Latitude, String Longitude);
}
