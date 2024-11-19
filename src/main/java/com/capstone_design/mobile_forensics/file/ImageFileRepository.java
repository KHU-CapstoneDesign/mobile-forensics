package com.capstone_design.mobile_forensics.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    @Override
    ImageFile save(ImageFile entity);

    @Query(value = "SELECT * FROM image_file u " +
            "WHERE ABS(TIMESTAMPDIFF(MINUTE, u.timestamp, :timestamp)) <= 30 " +
            "AND u.type = :type",
            nativeQuery = true)
    List<ImageFile> findAllWithin30Minutes(@Param("timestamp") LocalDateTime timestamp,
                                           @Param("type") String type);

}
