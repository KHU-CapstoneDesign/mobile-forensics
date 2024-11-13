package com.capstone_design.mobile_forensics.file;

import com.capstone_design.mobile_forensics.log.LogProcessService;
import com.capstone_design.mobile_forensics.log.LogProcessServiceImpl;
import com.capstone_design.mobile_forensics.web.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class FileService {

    @Autowired
    private WebService webService;
    @Autowired
    private LogProcessService logService;
    @Autowired
    private ImageFileRepository imageRepository;

    public ResponseEntity signal(String signal){

        // 파일 전송 시작과 끝 -> 프론트엔드에 알림(POST)
        if (signal.equalsIgnoreCase("start"))  {
            log.info("Processing Started.");
            ResponseEntity<String> response = webService.sendNotificationStart();
            return response;
        } else if (signal.equalsIgnoreCase("end")) {
            log.info("Processing Completed");
            ResponseEntity<String> response = webService.sendNotificationEnd();
            return response;
        }
        return null;
    }

    public ResponseEntity fileUpload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        log.info("File received.\tFile Name = {}", fileName);

        // 파일 확장자 확인
        // -> 이미지 파일이면 데이터베이스 저장
        // -> 로그 파일이면 로그 파싱 후 저장
        switch (getFileExtension(fileName).toLowerCase()) {
            case "txt" :
                logService.parseLogs(file); // 로그파일 처리
                break;
            case "jpg" :
            case "jpeg" :
            case "png" :
                processImageFile(file); // 이미지파일 처리
                break;
            default :
                throw new IllegalArgumentException("Unsupported File Type: " + fileName);

        }

        return null;
    }

    // 파일 확장자 추출
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex == -1) {
            throw new IllegalArgumentException("No file extension found for file: " + fileName);
        }
        return fileName.substring(lastIndex + 1);
    }

    // 이미지 파일 처리(저장)
    private ImageFile processImageFile(MultipartFile file) throws IOException {
        log.info("Processing Image File: " + file.getOriginalFilename());

        try {
            byte[] imageBytes = file.getBytes();
            ImageFile imageFile = new ImageFile(null, file.getOriginalFilename(), imageBytes, null);
            ImageFile saved = imageRepository.save(imageFile);
            return saved;
        } catch (IOException e) {
            throw new RuntimeException("Failed to Process Image File", e);
        }
    }


    private LocalDateTime extractTimeStamp(String fileName) {
        // 파일 이름이 "yyyyMMdd_HHmmss.jpg" 형식이므로, 확장자를 제거하고 날짜 부분만 추출
        String datePart = fileName.substring(0, fileName.indexOf("."));

        // DateTimeFormatter 생성, 파일 이름 형식에 맞춤
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        // LocalDateTime으로 변환
        return LocalDateTime.parse(datePart, formatter);
    }

}
