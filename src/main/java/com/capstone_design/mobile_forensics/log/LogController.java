package com.capstone_design.mobile_forensics.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class LogController {

    @Autowired
    private LogParserService logParserService;

    // 로그 파일을 분석하여 모든 로그 항목 반환
    @GetMapping("/logs")
    public List<LogEntry> getLogs(@RequestParam String filePath) throws IOException {
        return logParserService.parseLogs(filePath);
    }

}
