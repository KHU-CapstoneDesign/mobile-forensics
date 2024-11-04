package com.capstone_design.mobile_forensics.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class LogParserService {
    /**
     * Data Type - AppUsage(Drive, Ndrive, SnowCam, SodaCam)
     * - PictureTaken
     * - GPS Metadata
     * - Wifi
     *
     *
     * AppUsage Format (time, type, package, class, instanceId, taskRootClass)
     * PictureTaken Format (MM-dd HH:mm:ss) - 이건 로그 유무로 확인
     *
     * GPS Format (Date/Time {yyyy:MM:dd HH:mm:ss'Z'}, Position (아래 예시 deg) )
     * 37 deg 32' 42.78" N, 126 deg 44' 36.65" E -> 37°32'42.8"N 126°44'36.7"E
     *
     *
     */

    // 로그 데이터 시간 형식
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 로그 파일 읽고 파싱
    public List<LogEntry> parseLogs(String filePath) throws IOException
    {
        ArrayList<LogEntry> logs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                LogEntry log = parseLine(line);
                if(log != null) logs.add(log);
            }
        }
        return logs;
    }


    // 한 줄의 로그 파싱, ActivityLog 객체로 변환
    private LogEntry parseLine(String line)
    {
        if(line.contains("ACTIVITY_RESUMED")) {return parseActivityLog(line);}
//        else if (line.contains("ACTIVITY_PAUSED")) {return parseActivityLog(line);}
        else if(line.contains("onCaptureCompleted")) {return parseShootingLog(line);}
        else {log.info("Unknown log type: " + line); return null;}
    }

    // 드라이브, 사용자 카메라 앱 관련 로그 파싱 (App Usage)
    protected ActivityLog parseActivityLog(String line)
    {
        try {
            String[] parts = line.split(" ");
            String timePart = parts[0].split("=")[1] + " " + parts[1];
            LocalDateTime timestamp = LocalDateTime.parse(timePart, FORMATTER);
            log.info("timestamp = {}", timestamp);

            String type = parts[2].split("=")[1];
            String packageName = parts[3].split("=")[1];
            String className = parts[4].split("=")[1];

            log.info("type = {}, packageName = {}, className = {}", type, packageName, className);

            return new ActivityLog(timestamp, type, packageName, className);
        } catch (Exception e) {
            System.err.println("Error parsing application activity log: " + line);
            return null;
        }
    }

    // 사진촬영 이벤트 로그 파싱 (Picture Taken)
    protected ShootingLog parseShootingLog(String line)
    {
        try {
            // 정규식을 통해 필요한 데이터 파싱
            Pattern pattern = Pattern.compile(
                    "(\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\.\\d+\\s+\\d+\\s+\\d+\\s+(\\w+-\\d+):\\s+(\\w+)\\s+.+onCaptureCompleted");
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                // 날짜 및 시간 파싱 후 LocalDateTime 형식으로 변환
                String dateTimeStr = "2024-" + matcher.group(1);  // 연도를 추가하여 yyyy-MM-dd 형식으로 맞춤
                LocalDateTime timestamp = LocalDateTime.parse(dateTimeStr, FORMATTER);

                // 로그의 태그와 이벤트 타입 파싱
                String tag = matcher.group(2);
                String eventType = matcher.group(3);

                // ShootingLog 객체 생성 후 반환
                return new ShootingLog(timestamp, tag, eventType);
            } else {
                throw new IllegalArgumentException("로그 형식이 올바르지 않습니다.");
            }

        } catch (Exception e) {
            System.err.println("Error parsing camera shooting log: " + line);
            return null;
        }
    }
}
