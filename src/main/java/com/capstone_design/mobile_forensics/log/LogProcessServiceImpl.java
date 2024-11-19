package com.capstone_design.mobile_forensics.log;

import com.capstone_design.mobile_forensics.log.api.GeolocationService;
import com.capstone_design.mobile_forensics.log.dto.*;
import com.capstone_design.mobile_forensics.log.entity.*;
import com.capstone_design.mobile_forensics.log.repository.AppUsageRepository;
import com.capstone_design.mobile_forensics.log.repository.GPSMetadataRepository;
import com.capstone_design.mobile_forensics.log.repository.TakenPictureRepository;
import com.capstone_design.mobile_forensics.log.repository.WifiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class LogProcessServiceImpl implements LogProcessService {

    @Autowired
    private GeolocationService geolocationService;
    @Autowired
    private AppUsageRepository appUsageRepository;
    @Autowired
    private GPSMetadataRepository gpsMetadataRepository;
    @Autowired
    private TakenPictureRepository takenPictureRepository;
    @Autowired
    private WifiRepository wifiRepository;

    // [ 로그 데이터 시간 형식 ]
    private static final DateTimeFormatter appUsageFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter otherFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter gpsFORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    private static String extractYear() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        return String.valueOf(year);
    }

    // 로그 파일 읽고 파싱
    public ResponseEntity parseLogs(MultipartFile file) throws IOException
    {
        ArrayList<LogEntityEntry> logs = new ArrayList<>();

        // 사진 메타데이터 파일인 경우 - 파일 하나 통째로 파싱 (한줄씩 로그파싱X)
        if (file.getOriginalFilename().contains("metadata")) {
            log.info("Received File is GPS metadata File.");
            LogEntityEntry log = gpsMetadataProcess(file);
            if(log != null) logs.add(log);
        }
        else {

            // 나머지 로그 파일은 line 단위로 파싱
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
//                line = line.substring(0, line.length() - 1);
                    LogEntityEntry log = parseLine(line);
                    if (log != null) logs.add(log);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                return new ResponseEntity(HttpStatusCode.valueOf(503));
            }
        }
        if (!logs.isEmpty()) return new ResponseEntity(HttpStatusCode.valueOf(200));
        else return new ResponseEntity(HttpStatusCode.valueOf(204));
    }


    // 한 줄의 로그 파싱, Log파일의 라인을 읽어서, 각 로그파일 별로 분기
    // GPS 메타데이터는 포함되지 않는다
    public LogEntityEntry parseLine(String line)
    {
        // 앱 사용기록 로그파일인 경우
        if(line.contains("ACTIVITY_RESUMED")) {return parseAppUsageLogProcess(line);}
        // 카메라 촬영 로그파일인 경우
        else if(line.contains("onCaptureCompleted")) {return parseTakenPitureLogProcess(line);}
        // 와이파이 로그파일인 경우
        else if(line.contains("BSSID")) {return wifiLogProcess(line);}
        // 그 외 알 수 없는 파일
        else {log.info("Unknown log type: " + line); return null;}
    }

    // 드라이브, 사용자 카메라 앱 관련 로그 파싱 (App Usage)
    public AppUsageLog parseAppUsageLogProcess(String line)
    {
        log.info("Get AppUsageLog: " + line);
        try {
            String[] parts = line.trim().split(" ");

//            for (String part : parts) {
//                System.out.println(part);
//            }

            // time 부분 추출 후 따옴표 제거
            String timePart = parts[0].split("=")[1].replace("\"", "") + " " + parts[1].replace("\"", "");
            LocalDateTime timestamp = LocalDateTime.parse(timePart, appUsageFORMATTER);
            log.info("timestamp = {}", timestamp);

            String type = parts[2].split("=")[1];
            String packageName = parts[3].split("=")[1];
            String className = parts[4].split("=")[1];

            log.info("type = {}, packageName = {}, className = {}", type, packageName, className);

            AppUsageDTO dto = new AppUsageDTO(timestamp, type, packageName, className);
            try {
                AppUsageLog saved = saveAppUsageLog(dto);
                return saved;
            } catch(Exception ex) {
                log.error("Failed to Save AppUsageLog Entity: " + line);
                return null;
            }
        } catch (Exception e) {
            log.error("Error parsing application usage log: " + line);
            return null;
        }
    }

    // 사진촬영 이벤트 로그 파싱 (Picture Taken)
    public TakenPictureLog parseTakenPitureLogProcess(String line)
    {
        try {
//             정규식을 통해 필요한 데이터 파싱
            String[] parts = line.split(" ");

            String datePart = parts[0] + " " + parts[1];
            String dateTime = extractYear() + "-" + datePart;
            LocalDateTime timestamp = LocalDateTime.parse(dateTime, otherFORMATTER);
            String tag = parts[6];
            String eventType = parts[7];

            log.info("Timestamp={}, Tag={}, EventType={}", timestamp, tag, eventType);

            // ShootingLog 객체 생성 후 반환
            TakenPictureDTO dto = new TakenPictureDTO(timestamp, tag, eventType);
            try {
                TakenPictureLog saved = saveTakenPictureLog(dto);
                return saved;
            } catch (Exception e) {
                log.error("Failed to Save TakenPictureLog Entity: " + line);
                return null;
            }
        } catch (Exception e) {
            log.error("Error parsing camera shooting log: " + line);
            log.error(e.getMessage());
            return null;
        }
    }
    public WifiLog wifiLogProcess(String line) {
        String startTime = parseField(line, "startTime=", ",");
        String ssid = parseField(line, "SSID=\"", "\"");
        String bssid = parseField(line, "BSSID=", ",");
        Integer durationMillis = Integer.parseInt(parseField(line, "durationMillis=", ","));
        double signalStrength = Double.parseDouble(parseField(line, "signalStrength=", ","));
        Integer mChannelInfo = Integer.parseInt(parseField(line, "mChannelInfo=", ","));

        // startTime을 LocalDateTime으로 변환
        String dateTimeStr = extractYear() + "-" + startTime;  // 연도를 추가하여 yyyy-MM-dd 형식으로 맞춤
        LocalDateTime timestamp = LocalDateTime.parse(dateTimeStr, otherFORMATTER);

        // WifiLog 객체 생성 후 반환 <-- 추출한 위도, 경도 입력 필요
        WifiDTO dto = new WifiDTO(ssid, bssid, signalStrength, durationMillis, mChannelInfo, 0.0, 0.0, timestamp);
        WifiDTO locationsDTO = geolocationService.getLocation(dto);

        try {
            WifiLog saved = saveWifiLog(locationsDTO);
            return saved;
        } catch (Exception e) {
            log.error("Failed to Save WifiLog Entity: " + line);
            return null;
        }
    }

    private String parseField(String line, String start, String end) {
        int startIndex = line.indexOf(start) + start.length();
        int endIndex = line.indexOf(end, startIndex);
        return line.substring(startIndex, endIndex).trim();
    }

    public GPSMetadata gpsMetadataProcess(MultipartFile file) throws IOException {
        double latitude = 0.0;
        double longitude = 0.0;
        LocalDateTime timestamp = null;

        // 파일 내용을 BufferedReader로 읽기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            // 각 줄을 읽으면서 필요한 정보 추출
            while ((line = reader.readLine()) != null) {
                if (line.contains("GPS Date/Time")) {
                    // Date/Time 부분 추출하여 LocalDateTime으로 변환
                    String dateTimeStr = line.split(": ", 2)[1].replace("Z", "");
                    timestamp = LocalDateTime.parse(dateTimeStr, gpsFORMATTER);
                } else if (line.contains("GPS Latitude")) {
                    // Latitude 부분 추출
                    String lat = line.split(": ", 2)[1];
                    lat.replace("N", "");
                    latitude = Double.parseDouble(lat);
                } else if (line.contains("GPS Longitude")) {
                    // Longitude 부분 추출
                    String lng = line.split(": ", 2)[1];
                    lng.replace("E", "");
                    longitude = Double.parseDouble(lng);
                }
            }
        }

        // 추출한 정보를 기반으로 GPSMetadata 객체 생성
        GPSMetadataDTO dto = new GPSMetadataDTO(latitude, longitude, timestamp);
        try {
            GPSMetadata saved = saveGPSmetadata(dto);
            return saved;
        } catch (Exception e) {
            log.error("Failed to Save GPS Metadata Entity");
            return null;
        }
    }

    public AppUsageLog saveAppUsageLog(AppUsageDTO dto){
        AppUsageLog entity = dto.toEntity();
        AppUsageLog saved = appUsageRepository.save(entity);
        return saved;
    }
    public GPSMetadata saveGPSmetadata(GPSMetadataDTO dto){
        GPSMetadata entity = dto.toEntity();
        GPSMetadata saved = gpsMetadataRepository.save(entity);
        return saved;
    }
    public TakenPictureLog saveTakenPictureLog(TakenPictureDTO dto){
        TakenPictureLog entity = dto.toEntity();
        TakenPictureLog saved = takenPictureRepository.save(entity);
        return saved;
    }
    public WifiLog saveWifiLog(WifiDTO dto){
        WifiLog entity = dto.toEntity();
        WifiLog saved = wifiRepository.save(entity);
        return saved;
    }
}

/*
* 반경 50m
*
* 위도 1도 당, 110.9
* */

