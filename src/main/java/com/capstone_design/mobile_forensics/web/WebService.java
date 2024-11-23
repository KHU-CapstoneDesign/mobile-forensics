package com.capstone_design.mobile_forensics.web;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface WebService {

    ResponseEntity<UserData> saveUser(UserDTO request);
    UserData getUser(Long userId);

    ResponseEntity<String> sendNotificationStart();
    ResponseEntity<String> sendNotificationEnd();

    ResponseEntity countAllData(Long userId);

    ResponseEntity getCameraImages(Long userId) throws IOException;
    ResponseEntity getCloudImages(Long userId) throws IOException;
    ResponseEntity getCacheImages(Long userId) throws IOException;

    ResponseEntity getNaverCloud_AppUsage(Long userId);
    ResponseEntity getGoogleCloud_AppUsage(Long userId);

    ResponseEntity getSnow_AppUsage(Long userId);
    ResponseEntity getSoda_AppUsage(Long userId);

    ResponseEntity getPictureTakenLog(Long userId);
    ResponseEntity getLocationLog(Long userId);

    ResponseEntity exit(Long userId);

}
