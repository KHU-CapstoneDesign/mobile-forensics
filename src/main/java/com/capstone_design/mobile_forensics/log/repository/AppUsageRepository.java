package com.capstone_design.mobile_forensics.log.repository;

import com.capstone_design.mobile_forensics.log.entity.AppUsageLog;
import com.capstone_design.mobile_forensics.log.entity.GPSMetadata;
import com.capstone_design.mobile_forensics.log.entity.TakenPictureLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface AppUsageRepository extends JpaRepository<AppUsageLog, Long> {
    @Query(value = "SELECT * FROM app_usage_log u WHERE " +
            "ABS(TIMESTAMPDIFF(MINUTE, u.timestamp, :timestamp)) <= 30",
            nativeQuery = true)
    List<AppUsageLog> findAllWithin30Minutes(@Param("timestamp") LocalDateTime timestamp);

    @Query(value = "SELECT * FROM app_usage_log u WHERE " +
            "ABS(TIMESTAMPDIFF(MINUTE, u.timestamp, :timestamp)) <= 30 AND " +
            "u.package_name LIKE %:keyword%",
            nativeQuery = true)
    List<AppUsageLog> findAllWithin30MinutesAndPackageContaining(@Param("timestamp") LocalDateTime timestamp,
                                                                 @Param("keyword") String keyword);
}
