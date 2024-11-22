package com.capstone_design.mobile_forensics.log;

import com.capstone_design.mobile_forensics.log.dto.*;
import com.capstone_design.mobile_forensics.log.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LogProcessService {

    ResponseEntity parseLogs(MultipartFile file) throws IOException;
    LogEntityEntry parseLine(String line);

    AppUsageLog parseAppUsageLogProcess(String line);
    TakenPictureLog parseTakenPitureLogProcess(String line);
    WifiLog wifiLogProcess(String line);
    GPSMetadata gpsMetadataProcess(MultipartFile file) throws IOException;

    AppUsageLog saveAppUsageLog(AppUsageDTO dto);
    TakenPictureLog saveTakenPictureLog(TakenPictureDTO dto);
    WifiLog saveWifiLog(WifiDTO dto);
    GPSMetadata saveGPSmetadata(GPSMetadataDTO dto);
}
