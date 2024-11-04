package com.capstone_design.mobile_forensics.log;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLog implements LogEntry{
    private LocalDateTime timestamp;
    private String type;
    private String packageName;
    private String className;

    /*
    * Activity Log - APP Usage 공용으로 사용
    * 추후 패키지명으로 drive, cam 분별 (String.contains)
    * */

    @Override
    public String getLogType() {
        return "Application Activity Log";
    }
    @Override
    public String toString() {
        return timestamp + " | " + type + " | " + packageName + " | " + className;
    }
}