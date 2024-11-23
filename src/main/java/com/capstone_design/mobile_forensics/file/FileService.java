package com.capstone_design.mobile_forensics.file;

import com.capstone_design.mobile_forensics.file.api.SafeSearchResponse;
import com.capstone_design.mobile_forensics.web.UserData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface FileService {
    ResponseEntity signal(String signal);
    void fileUpload(MultipartFile file, String parentDir) throws IOException;
    String getFileExtension(String fileName);
    LocalDateTime getTimestamp(String fileName);
    String determineImageType(String parentDir);
    ResponseEntity processImageFile(MultipartFile file, String parentDir) throws IOException;
    List<SafeSearchResponse> analyzeAndRespondImages(UserData userData, String parentDir) throws IOException;

}
