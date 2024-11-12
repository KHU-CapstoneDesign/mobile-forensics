package com.capstone_design.mobile_forensics.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
@Slf4j
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/signal")
    public ResponseEntity<String> handleSignal(@RequestBody String signal) {
        ResponseEntity res = fileService.signal(signal);
        return res;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        fileService.fileUpload(file);
        return null;
    }
}
