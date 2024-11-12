package com.capstone_design.mobile_forensics.log.repository;

import com.capstone_design.mobile_forensics.log.entity.AppUsageLog;
import com.capstone_design.mobile_forensics.log.entity.GPSMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppUsageRepository extends LogRepository {
    List<AppUsageLog> findAllByTimestamp(LocalDateTime timestamp);
}
