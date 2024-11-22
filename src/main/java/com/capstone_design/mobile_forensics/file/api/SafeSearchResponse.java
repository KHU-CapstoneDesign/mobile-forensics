package com.capstone_design.mobile_forensics.file.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SafeSearchResponse {
    private String fileName;
    private LocalDateTime timestamp;
    private String parent; //type : camera, trash, soda, drive
    private String imageData; // Base64로 인코딩된 이미지 데이터
    private AnalysisResult result;
}
