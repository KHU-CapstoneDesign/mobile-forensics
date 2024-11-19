package com.capstone_design.mobile_forensics.log.repository;


import com.capstone_design.mobile_forensics.log.entity.TakenPictureLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface TakenPictureRepository extends JpaRepository<TakenPictureLog, Long> {

    @Query(value = "SELECT * FROM picture_taken_log u WHERE " +
            "ABS(TIMESTAMPDIFF(MINUTE, u.timestamp, :timestamp)) <= 30",
            nativeQuery = true)
    List<TakenPictureLog> findAllWithin30Minutes(@Param("timestamp") LocalDateTime timestamp);
}

