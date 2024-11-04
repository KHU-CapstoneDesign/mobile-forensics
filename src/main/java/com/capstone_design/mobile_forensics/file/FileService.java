package com.capstone_design.mobile_forensics.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileService {

    public ResponseEntity fileUpload(MultipartFile file) {
        log.info("Log-file Served from Batch");
        try {
            String uploadDir = "/path/to/uplaod";
            Path path = Paths.get(uploadDir + file.getOriginalFilename());

            Files.write(path, file.getBytes());

            /* path에 S3 경로를 집어넣을 것,
             *  저장된 경로를 DB에 저장할 것
             *  */

            log.info("파일이 업로드 되었습니다: " + file.getOriginalFilename());
            return ResponseEntity.ok("File uploaded successfully");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }
}
