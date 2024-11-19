package com.capstone_design.mobile_forensics.web;

import com.capstone_design.mobile_forensics.web.ResponseDTO.WholeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class WebService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataDetectService dataDetectService;

    public ResponseEntity<UserData> saveUser(UserDTO request) {
        log.info("data={}", request);
        UserData userData = request.toEntity();
        try {
            UserData saved = userRepository.save(userData);
            Long userId = saved.getUserId();

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/api/result/user"));
            headers.add("Set-Cookie", "userId=" + userId + "; Path=/api/result; HttpOnly");

            return new ResponseEntity<>(headers, HttpStatusCode.valueOf(303));
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(503));
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

    public ResponseEntity countAllData(Long userId) {
        Optional<UserData> byId = userRepository.findById(userId);
        UserData user = byId.orElseThrow(()-> new RuntimeException("Cannot Find User. Please Rewrite User Data(Location, DateTime)."));
        log.info(user.toString());
        try {
            WholeData data = findData(user);
            log.info("+++++ whole data +++++");
            log.info(data.toString());
            log.info(data.getGps().toString());

            return new ResponseEntity<>(data, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(503));
        }
    }

    private WholeData findData(UserData user){
        return new WholeData().builder()
                .images(dataDetectService.getImageFile(user))
                .pictureTaken(dataDetectService.getCountTakenPictureLog(user))
                .gps(new WholeData.GPS().builder()
                        .metadata(dataDetectService.getCountGPSmetadata(user))
                        .wifi(dataDetectService.getCountWifiLog(user))
                        .build())
                .appUsage(dataDetectService.getCountAppUsageLog(user))
                .cache(dataDetectService.getCacheImage(user))
                .build();
    }

}
