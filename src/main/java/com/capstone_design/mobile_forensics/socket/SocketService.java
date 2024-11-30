package com.capstone_design.mobile_forensics.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service @Slf4j
public class SocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public SocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public ResponseEntity signal(String signal){

        // 파일 전송 시작과 끝 -> 프론트엔드에 알림(POST)
        if (signal.equalsIgnoreCase("start"))  {
            log.info("Processing Started.");
            ResponseEntity response = sendNotificationStart();
            return response;
        } else if (signal.equalsIgnoreCase("end")) {
            log.info("Processing Completed");
            ResponseEntity response = sendNotificationEnd();
            return response;
        }
        return null;
    }

    public ResponseEntity sendNotificationStart(){
        String message = "Data Processing Started";
        try {
            messagingTemplate.convertAndSend("/signal/notify", message);
            return new ResponseEntity("Message Sent to Client.", HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Failed Sending Start Signal to Client.", HttpStatusCode.valueOf(503));
        }
    }

    public ResponseEntity<String> sendNotificationEnd(){
        String message = "Data Processing Completed";
        try {
            messagingTemplate.convertAndSend("/signal/notify", message);
            return new ResponseEntity("Message Sent to Client.", HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Failed Sending End Signal to Client.", HttpStatusCode.valueOf(503));
        }
    }
}
