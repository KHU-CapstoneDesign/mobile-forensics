package com.capstone_design.mobile_forensics.socket;

import com.capstone_design.mobile_forensics.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocketNotifyController {
    @Autowired
    private SocketService socketService;

    @PostMapping("/api/signal")
    public ResponseEntity handleSignal(@RequestParam String signal) {
        ResponseEntity res = socketService.signal(signal);
        return res;
    }
}
