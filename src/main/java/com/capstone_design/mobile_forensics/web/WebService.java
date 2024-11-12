package com.capstone_design.mobile_forensics.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@Slf4j
public class WebService {
    @Autowired
    private UserRepository userRepository;

    public String saveUser(LocalDateTime dateTime, String lat, String lon) {
        UserData userData = new UserData(null, lat, lon, dateTime);

        UserData saved = userRepository.save(userData);
        return saved.toString();
    }

    public void sendNotificationStart(){
        String url = "http://frontend-server.com/api/notify-start";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = "{ \"message\": \"Data processing started\" }";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        log.info("Notification sent to frontend: " + response.getStatusCode());
    }

    public void sendNotificationEnd(){
        String url = "http://frontend-server.com/api/notify-end";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = "{ \"message\": \"Data processing complete\" }";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        log.info("Notification sent to frontend: " + response.getStatusCode());
    }



}
