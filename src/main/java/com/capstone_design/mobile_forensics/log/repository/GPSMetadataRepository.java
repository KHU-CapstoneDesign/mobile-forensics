package com.capstone_design.mobile_forensics.log.repository;

import com.capstone_design.mobile_forensics.log.entity.AppUsageLog;
import com.capstone_design.mobile_forensics.log.entity.GPSMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface GPSMetadataRepository extends JpaRepository<GPSMetadata, Long> {

    // 30분 전후로 범위 설정하고 50m 이내로 거리 계산하여 필터링하는 Query
    @Query(value = "SELECT * FROM gps_image_metadata u WHERE " +
            "(ABS(TIMESTAMPDIFF(MINUTE, u.timestamp, :timestamp)) <= 30) AND " + // 시간 범위 (±30분)
            "(6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(u.latitude)) * COS(RADIANS(u.longitude) - RADIANS(:longitude)) + SIN(RADIANS(:latitude)) * SIN(RADIANS(u.latitude)))) <= 0.05",
            nativeQuery = true) // 거리 범위 (50m)
    List<GPSMetadata> findByTimestampAndLocationRange(@Param("timestamp") LocalDateTime timestamp,
                                                      @Param("latitude") double latitude,
                                                      @Param("longitude") double longitude);
}
