package com.capstone_design.mobile_forensics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    String testReply() {
        return "Test";
    }
}
