package com.capstone_design.mobile_forensics;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@Component
public class GoogleInitializer {
    // JSON 키 파일 경로
    @Value("${google.cloud.credentials.path}")
    private String credentialsPath;

    private ImageAnnotatorClient visionClient;

    @PostConstruct
    public void setupGoogleCredentials() throws IOException {
        System.out.println("Credentials Path: " + credentialsPath);
        File file = new File(credentialsPath);
        System.out.println("File Exists: " + file.exists());
        try {
            // 인증 정보 로드
            ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialsPath));
            // 클라이언트 설정
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                    .setCredentialsProvider(() -> credentials) // 인증 정보 제공
                    .build();
            // Vision API 클라이언트 생성
            visionClient = ImageAnnotatorClient.create(settings);
            log.info("Vision API 클라이언트 초기화 완료");
        } catch(Exception e) {
            throw new RuntimeException("Vision API 클라이언트 초기화 중 오류 발생", e);
        }
    }

    public ImageAnnotatorClient getVisionClient() {
        if (visionClient == null) {
            throw new IllegalStateException("Vision API 클라이언트가 초기화되지 않았습니다");
        }
        return visionClient;
    }
}

