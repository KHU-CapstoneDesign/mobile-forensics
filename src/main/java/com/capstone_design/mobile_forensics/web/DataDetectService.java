package com.capstone_design.mobile_forensics.web;

import com.capstone_design.mobile_forensics.file.ImageFile;
import com.capstone_design.mobile_forensics.file.ImageFileRepository;
import com.capstone_design.mobile_forensics.log.dto.LocationDTO;
import com.capstone_design.mobile_forensics.log.entity.AppUsageLog;
import com.capstone_design.mobile_forensics.log.entity.GPSMetadata;
import com.capstone_design.mobile_forensics.log.entity.TakenPictureLog;
import com.capstone_design.mobile_forensics.log.entity.WifiLog;
import com.capstone_design.mobile_forensics.log.repository.AppUsageRepository;
import com.capstone_design.mobile_forensics.log.repository.GPSMetadataRepository;
import com.capstone_design.mobile_forensics.log.repository.TakenPictureRepository;
import com.capstone_design.mobile_forensics.log.repository.WifiRepository;
import com.capstone_design.mobile_forensics.web.ResponseDTO.WholeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;

@Service
@Slf4j
public class DataDetectService {
    @Autowired
    private AppUsageRepository appUsageRepository;
    @Autowired
    private GPSMetadataRepository gpsMetadataRepository;
    @Autowired
    private TakenPictureRepository takenPictureRepository;
    @Autowired
    private WifiRepository wifiRepository;
    @Autowired
    private ImageFileRepository imageFileRepository;

    public Long getCountCloudAppUsageLog(UserData user) {
        List<AppUsageLog> ndrive = appUsageRepository.findAllWithin30MinutesAndPackageContaining(user.getDateTime(), "ndrive");
        List<AppUsageLog> google = appUsageRepository.findAllWithin30MinutesAndPackageContaining(user.getDateTime(), "google");
        List<AppUsageLog> result = Stream.concat(ndrive.stream(), google.stream()).collect(Collectors.toList());

        for (AppUsageLog l : result) {
            log.info(l.toString());
        }
        return result.stream().count();
    }
    public Long getCountCameraAppUsageLog(UserData user) {
        List<AppUsageLog> snow = appUsageRepository.findAllWithin30MinutesAndPackageContaining(user.getDateTime(), "campmobile");
        List<AppUsageLog> soda = appUsageRepository.findAllWithin30MinutesAndPackageContaining(user.getDateTime(), "soda");
        List<AppUsageLog> result = Stream.concat(snow.stream(), soda.stream()).collect(Collectors.toList());
        for (AppUsageLog l : result) {
            log.info(l.toString());
        }
        return result.stream().count();
    }

    public Long getCountTakenPictureLog(UserData user) {
        List<TakenPictureLog> allLogs = takenPictureRepository.findAllWithin30Minutes(user.getDateTime());
        for (TakenPictureLog allLog : allLogs) {
            log.info(allLog.toString());
        }
        return allLogs.stream().count();
    }

    public Long getCountGPSmetadata(UserData user) {
        List<GPSMetadata> allLogs = gpsMetadataRepository.findByTimestampAndLocationRange(user.getDateTime(), user.getLatitude(), user.getLongitude());
        for (GPSMetadata allLog : allLogs) {
            log.info(allLog.toString());
        }
        return allLogs.stream().count();
    }

    public Long getCountWifiLog(UserData user) {
        List<WifiLog> allLogs = wifiRepository.findByTimestampAndLocationRange(user.getDateTime(), user.getLatitude(), user.getLongitude());
        for (WifiLog allLog : allLogs) {
            log.info(allLog.toString());
        }
        return allLogs.stream().count();
    }

    public Long getDriveCacheImage(UserData user) {
        List<ImageFile> allMyboxCaches = imageFileRepository.findAllWithin30Minutes(user.getDateTime(), "drive");
        log.info("======== Drive-Cache ======");
        for (ImageFile allMyboxCach : allMyboxCaches) {
            log.info(allMyboxCach.toString());
        }
        return allMyboxCaches.stream().count();
    }
    public Long getSodaCacheImage(UserData user) {
        List<ImageFile> allSodaCaches = imageFileRepository.findAllWithin30Minutes(user.getDateTime(), "soda");
        log.info("======== Soda-Cache ======");
        for (ImageFile allSodaCach : allSodaCaches) {
            log.info(allSodaCach.toString());
        }
        return allSodaCaches.stream().count();
    }

    public Long getImageFile(UserData user) {
        List<ImageFile> allCamera = imageFileRepository.findAllWithin30Minutes(user.getDateTime(), "camera");
        log.info("======== Camera-Image ======");
        for (ImageFile imageFile : allCamera) {
            log.info(imageFile.toString());
        }
        List<ImageFile> allTrash = imageFileRepository.findAllWithin30Minutes(user.getDateTime(), "trash");
        log.info("======== Trash-Image ======");
        for (ImageFile trash : allTrash) {
            log.info(trash.toString());
        }
        return allCamera.stream().count() + allTrash.stream().count();
    }


}
