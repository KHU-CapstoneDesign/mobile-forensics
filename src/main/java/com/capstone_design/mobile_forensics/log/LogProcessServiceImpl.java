package com.capstone_design.mobile_forensics.log;

import com.capstone_design.mobile_forensics.log.dto.*;
import com.capstone_design.mobile_forensics.log.entity.*;
import com.capstone_design.mobile_forensics.log.repository.AppUsageRepository;
import com.capstone_design.mobile_forensics.log.repository.GPSMetadataRepository;
import com.capstone_design.mobile_forensics.log.repository.TakenPictureRepository;
import com.capstone_design.mobile_forensics.log.repository.WifiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    /**
     * AppUsage Format (time, type, package, class, instanceId, taskRootClass)
     * PictureTaken Format (MM-dd HH:mm:ss) - 이건 로그 유무로 확인
     * GPS Format (Date/Time {yyyy:MM:dd HH:mm:ss'Z'}, Position (아래 예시 deg) )
     * 37 deg 32' 42.78" N, 126 deg 44' 36.65" E -> 37°32'42.8"N 126°44'36.7"E
     */
    // Repositories
    @Autowired
    private AppUsageRepository appUsageRepository;
    @Autowired
    private GPSMetadataRepository gpsMetadataRepository;
    @Autowired
    private TakenPictureRepository takenPictureRepository;
    @Autowired
    private WifiRepository wifiRepository;

    // 로그 데이터 시간 형식
    private static final DateTimeFormatter appUsageFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter otherFORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");
    private static final DateTimeFormatter gpsFORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    private static String extractYear() {
        LocalDate now = LocalDate.now();
        int year = now.getDayOfYear();
        return String.valueOf(year);
    }

    // 로그 파일 읽고 파싱
    public List<LogEntityEntry> parseLogs(MultipartFile file) throws IOException
    {
        ArrayList<LogEntityEntry> logs = new ArrayList<>();
        // 사진 메타데이터 파일인 경우 - 파일 하나 통째로 파싱 (한줄씩 로그파싱X)
        if (file.getOriginalFilename().contains("metadata")) { LogEntityEntry log = gpsMetadataProcess(file);}

        // 나머지 로그 파일은 line 단위로 파싱
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream())))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                LogEntityEntry log = parseLine(line);
                if(log != null) logs.add(log);
            }
        }
        return logs;
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
        try {
            String[] parts = line.split(" ");
            String timePart = parts[0].split("=")[1] + " " + parts[1];
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
                System.err.println("Failed to Save AppUsageLog Entity: " + line);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error parsing application usage log: " + line);
            return null;
        }
    }

    // 사진촬영 이벤트 로그 파싱 (Picture Taken)
    public TakenPictureLog parseTakenPitureLogProcess(String line)
    {
        try {
            // 정규식을 통해 필요한 데이터 파싱
            Pattern pattern = Pattern.compile(
                    "(\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\.\\d+\\s+\\d+\\s+\\d+\\s+(\\w+-\\d+):\\s+(\\w+)\\s+.+onCaptureCompleted");
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                // 날짜 및 시간 파싱 후 LocalDateTime 형식으로 변환

                String dateTimeStr = extractYear() + "-" + matcher.group(1);  // 연도를 추가하여 yyyy-MM-dd 형식으로 맞춤
                LocalDateTime timestamp = LocalDateTime.parse(dateTimeStr, otherFORMATTER);

                // 로그의 태그와 이벤트 타입 파싱
                String tag = matcher.group(2);
                String eventType = matcher.group(3);

                // ShootingLog 객체 생성 후 반환
                TakenPictureDTO dto = new TakenPictureDTO(timestamp, tag, eventType);
                try {
                    TakenPictureLog saved = saveTakenPictureLog(dto);
                    return saved;
                } catch (Exception e) {
                    System.err.println("Failed to Save TakenPictureLog Entity: " + line);
                    return null;
                }
            } else {
                throw new IllegalArgumentException("로그 형식이 올바르지 않습니다.");
            }
        } catch (Exception e) {
            System.err.println("Error parsing camera shooting log: " + line);
            return null;
        }
    }
    public WifiLog wifiLogProcess(String line) {
        // 정규식을 이용해 SSID, BSSID, startTime 추출
        Pattern pattern = Pattern.compile(
                "startTime=(\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+), SSID=\"([^\"]+)\", BSSID=([^\s,]+)");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            // 추출된 값들
            String startTime = matcher.group(1);
            String ssid = matcher.group(2);
            String bssid = matcher.group(3);

            // startTime을 LocalDateTime으로 변환
            String dateTimeStr = extractYear() + "-" + matcher.group(1);  // 연도를 추가하여 yyyy-MM-dd 형식으로 맞춤
            LocalDateTime timestamp = LocalDateTime.parse(dateTimeStr, otherFORMATTER);

            extractYear();
            /*
            * BSSID로 위도, 경도 추출 필요
            * */

            // WifiLog 객체 생성 후 반환 <-- 추출한 위도, 경도 입력 필요
            WifiDTO dto = new WifiDTO(ssid, bssid, null, null, timestamp);
            try {
                WifiLog saved = saveWifiLog(dto);
                return saved;
            } catch (Exception e) {
                System.err.println("Failed to Save WifiLog Entity: " + line);
                return null;
            }
        } else {
            // 형식이 맞지 않으면 null 반환
            System.err.println("Failed to parse log line: " + line);
            return null;
        }
    }

    public GPSMetadata gpsMetadataProcess(MultipartFile file) throws IOException {
        String latitude = null;
        String longitude = null;
        LocalDateTime timestamp = null;

        // 파일 내용을 BufferedReader로 읽기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            // 각 줄을 읽으면서 필요한 정보 추출
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("GPS Date/Time")) {
                    // Date/Time 부분 추출하여 LocalDateTime으로 변환
                    String dateTimeStr = line.split(": ", 2)[1].replace("Z", "");
                    timestamp = LocalDateTime.parse(dateTimeStr, gpsFORMATTER);
                } else if (line.startsWith("GPS Latitude")) {
                    // Latitude 부분 추출
                    latitude = line.split(": ", 2)[1];
                } else if (line.startsWith("GPS Longitude")) {
                    // Longitude 부분 추출
                    longitude = line.split(": ", 2)[1];
                }
            }
        }

        // 추출한 정보를 기반으로 GPSMetadata 객체 생성
        GPSMetadataDTO dto = new GPSMetadataDTO(latitude, longitude, timestamp);
        try {
            GPSMetadata saved = saveGPSmetadata(dto);
            return saved;
        } catch (Exception e) {
            System.err.println("Failed to Save GPS Metadata Entity");
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

