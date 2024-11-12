package com.capstone_design.mobile_forensics.log.repository;


import com.capstone_design.mobile_forensics.log.entity.TakenPictureLog;

import java.time.LocalDateTime;
import java.util.List;

public interface TakenPictureRepository extends LogRepository{
    List<TakenPictureLog> findAllByTimestamp(LocalDateTime timestamp);

}
