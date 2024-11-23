//package com.capstone_design.mobile_forensics.log.repository;
//
//import com.capstone_design.mobile_forensics.log.entity.AppUsageLog;
//import com.capstone_design.mobile_forensics.log.entity.LogEntityEntry;
//import org.springframework.data.domain.Example;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public interface LogRepository extends JpaRepository<LogEntityEntry, Long> {
//    @Override
//    <S extends LogEntityEntry> S save(S entity);
//
//    @Override
//    <S extends LogEntityEntry> List<S> findAll(Example<S> example);
//
//    <S extends LogEntityEntry> List<S> findAllByTimestamp(LocalDateTime timestamp);
//
//    <S extends LogEntityEntry> List<S> findAllByLatitudeAndLongitude(String Latitude, String Longitude);
//}
