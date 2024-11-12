package com.capstone_design.mobile_forensics.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    @Override
    ImageFile save(ImageFile entity);

    List<ImageFile> findAllByTimestamp(LocalDateTime timestamp);
}
