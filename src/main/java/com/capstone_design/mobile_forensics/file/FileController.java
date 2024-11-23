package com.capstone_design.mobile_forensics.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@Slf4j
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/signal")
    public ResponseEntity<String> handleSignal(@RequestParam String signal) {
        ResponseEntity res = fileService.signal(signal);
        return res;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("parentDir")String parentDir) throws IOException {
        fileService.fileUpload(file, parentDir);
        return null;
    }
}
