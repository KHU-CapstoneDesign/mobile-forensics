package com.capstone_design.mobile_forensics.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class WebService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<UserData> saveUser(UserDTO request) {
        log.info("data={}", request);
        UserData userData = request.toEntity();
        try {
            UserData saved = userRepository.save(userData);
            return new ResponseEntity<>(saved, HttpStatusCode.valueOf(200));
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(300));
        }

    }

    public ResponseEntity<String> sendNotificationStart(){
        String url = "http://localhost/api/signal";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = "{ \"message\": \"Data processing started\" }";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        log.info("Request = {}", requestEntity);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        log.info("Notification sent to frontend: " + response.getStatusCode());
        return response;
    }

    public ResponseEntity<String> sendNotificationEnd(){
        String url = "http://frontend-server.com/api/signal";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = "{ \"message\": \"Data processing complete\" }";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        log.info("Request = {}", requestEntity);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        log.info("Notification sent to frontend: " + response.getStatusCode());
        return response;
    }



}
