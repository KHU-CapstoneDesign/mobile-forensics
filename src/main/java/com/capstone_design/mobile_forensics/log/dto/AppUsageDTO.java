package com.capstone_design.mobile_forensics.log.dto;
import com.capstone_design.mobile_forensics.log.entity.AppUsageLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUsageDTO implements LogEntry {
    private LocalDateTime timestamp;
    private String type; // ACTIVITY_RESUMED
    /*
    * <Drive App> - package.split(".").[1] == "google" or "nhn"로 구분
    *   - Google : package(com.google.android.apps.docs) | class(com.google.android.apps.docs.drive.startup.StartupActivity)
    *   - Naver : package(com.nhn.android.ndrive) | class(com.nhn.android.ndrive.ui.SplashActivity)
    *
    * <Cam App> - package.split(".").[2] == "soda" or "snow"로 구분
    *   - Soda : package(com.snowcorp.soda.android) | class(com.linecorp.sodacam.android.camera.CameraActivity)
    *   - Snow : package(com.campmobile.snow) | class(com.linecorp.b612.android.activity.ActivityCamera)
    * */
    private String packageName;
    private String className;

    /*
    * Activity Log - APP Usage 공용으로 사용
    * 추후 패키지명으로 drive, cam 분별 (String.contains)
    * */

    @Override
    public String getLogType() {
        return "Application Usage Log";
    }
    @Override
    public String toString() {
        return timestamp + " | " + type + " | " + packageName + " | " + className;
    }

    public AppUsageLog toEntity(){
        return AppUsageLog.builder()
                .className(className)
                .packageName(packageName)
                .type(type)
                .timestamp(timestamp)
                .build();
    }

}