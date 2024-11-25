package com.capstone_design.mobile_forensics.web;

import com.capstone_design.mobile_forensics.file.FileService;
import com.capstone_design.mobile_forensics.file.ImageFileRepository;
import com.capstone_design.mobile_forensics.file.api.SafeSearchResponse;
import com.capstone_design.mobile_forensics.log.dto.LocationDTO;
import com.capstone_design.mobile_forensics.log.entity.AppUsageLog;
import com.capstone_design.mobile_forensics.log.entity.GPSMetadata;
import com.capstone_design.mobile_forensics.log.entity.TakenPictureLog;
import com.capstone_design.mobile_forensics.log.entity.WifiLog;
import com.capstone_design.mobile_forensics.log.repository.AppUsageRepository;
import com.capstone_design.mobile_forensics.log.repository.GPSMetadataRepository;
import com.capstone_design.mobile_forensics.log.repository.TakenPictureRepository;
import com.capstone_design.mobile_forensics.log.repository.WifiRepository;
import com.capstone_design.mobile_forensics.web.ResponseDTO.WholeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class WebServiceImpl implements WebService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataDetectService dataDetectService;
    @Autowired
    private FileService fileService;
    @Autowired
    private AppUsageRepository appUsageRepository;
    @Autowired
    private TakenPictureRepository takenPictureRepository;
    @Autowired
    private GPSMetadataRepository gpsMetadataRepository;
    @Autowired
    private WifiRepository wifiRepository;
    @Autowired
    private ImageFileRepository imageFileRepository;

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

    public UserData getUser(Long userId) {
        Optional<UserData> byId = userRepository.findById(userId);
        UserData userData = byId.orElseThrow(() -> new RuntimeException("Failed to Find User."));
        return userData;
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
                .appUsage(new WholeData.AppUsage().builder()
                        .cloud(dataDetectService.getCountCloudAppUsageLog(user))
                        .camera(dataDetectService.getCountCameraAppUsageLog(user))
                        .build())
                .cache(new WholeData.Cache().builder()
                        .mybox(dataDetectService.getDriveCacheImage(user))
                        .soda(dataDetectService.getSodaCacheImage(user))
                        .build())
                .build();
    }

    public ResponseEntity getCameraImages(Long userId) throws IOException {
        UserData user = getUser(userId);
        try {
            List<SafeSearchResponse> cameras = fileService.analyzeAndRespondImages(user, "camera");
            List<SafeSearchResponse> trashes = fileService.analyzeAndRespondImages(user, "trash");
            List<SafeSearchResponse> result = Stream.concat(cameras.stream(), trashes.stream()).collect(Collectors.toList());
            if(!result.isEmpty()) {
                for (SafeSearchResponse r : result) {
                    log.info(r.toString());
                }
                return new ResponseEntity(result, HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity("No Image Data", HttpStatusCode.valueOf(204));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Can't Response From Image Analysis Service(Google Vision API).");
        }
    }

    public ResponseEntity getCloudImages(Long userId) throws IOException {
        UserData user = getUser(userId);
        List<SafeSearchResponse> drive = fileService.analyzeAndRespondImages(user, "drive");
        return new ResponseEntity(drive, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity getCacheImages(Long userId) throws IOException {
        UserData user = getUser(userId);
        List<SafeSearchResponse> cache = fileService.analyzeAndRespondImages(user, "cache");
        return new ResponseEntity(cache, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity getNaverCloud_AppUsage(Long userId) {
        UserData user = getUser(userId);
        try {
            List<AppUsageLog> ndrive = appUsageRepository.findAllWithin30MinutesAndPackageContaining(user.getDateTime(), "ndrive");
            return new ResponseEntity(ndrive, HttpStatusCode.valueOf(200));

        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Can't Find Naver MYBOX App Usage Logs.", HttpStatusCode.valueOf(404));
        }
    }
    public ResponseEntity getGoogleCloud_AppUsage(Long userId) {
        UserData user = getUser(userId);
        try {
            List<AppUsageLog> google = appUsageRepository.findAllWithin30MinutesAndPackageContaining(user.getDateTime(), "google");
            return new ResponseEntity(google, HttpStatusCode.valueOf(200));
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Can't Find Google Drive App Usage Logs.", HttpStatusCode.valueOf(404));
        }
    }
    public ResponseEntity getSnow_AppUsage(Long userId) {
        UserData user = getUser(userId);
        try {
            List<AppUsageLog> snow = appUsageRepository.findAllWithin30MinutesAndPackageContaining(user.getDateTime(), "campmobile");
            return new ResponseEntity(snow, HttpStatusCode.valueOf(200));
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Can't Find Snow App Usage Logs.", HttpStatusCode.valueOf(404));
        }
    }
    public ResponseEntity getSoda_AppUsage(Long userId) {
        UserData user = getUser(userId);
        try {
            List<AppUsageLog> soda = appUsageRepository.findAllWithin30MinutesAndPackageContaining(user.getDateTime(), "soda");
            return new ResponseEntity(soda, HttpStatusCode.valueOf(200));
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Can't Find Soda App Usage Logs.", HttpStatusCode.valueOf(404));
        }
    }

    public ResponseEntity getPictureTakenLog(Long userId) {
        UserData user = getUser(userId);
        try {
            List<TakenPictureLog> takenLogs = takenPictureRepository.findAllWithin30Minutes(user.getDateTime());
            return new ResponseEntity(takenLogs, HttpStatusCode.valueOf(200));
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Can't Find Picture Taken Logs.", HttpStatusCode.valueOf(404));
        }
    }

    public ResponseEntity getLocationLog(Long userId) {
        UserData user = getUser(userId);
        try {
            //GPS 데이터 가져오기
            List<GPSMetadata> gps = gpsMetadataRepository.findByTimestampAndLocationRange(user.getDateTime(), user.getLatitude(), user.getLongitude());
            List<LocationDTO> locationGPS = gps.stream().map(gpsMetadata -> new LocationDTO(gpsMetadata.getLatitude(), gpsMetadata.getLongitude(), gpsMetadata.getTimestamp())).collect(Collectors.toList());
            //WiFi 데이터 가져오기
            List<WifiLog> wifi = wifiRepository.findByTimestampAndLocationRange(user.getDateTime(), user.getLatitude(), user.getLongitude());
            List<LocationDTO> locationWIFI = wifi.stream().map(wifiLog -> new LocationDTO(wifiLog.getLatitude(), wifiLog.getLongitude(), wifiLog.getTimestamp())).collect(Collectors.toList());
            // 하나의 데이터 타입(위도, 경도, 시간)으로 통일 및 데이터 합치기
            List<LocationDTO> result = Stream.concat(locationWIFI.stream(), locationGPS.stream()).collect(Collectors.toList());
            return new ResponseEntity(result, HttpStatusCode.valueOf(200));
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity("Can't Find Location Data From Image metadata & WiFi BSSID", HttpStatusCode.valueOf(404));
        }
    }

    public ResponseEntity exit(Long userId) {
        UserData user = getUser(userId);
        userRepository.delete(user);

        gpsMetadataRepository.deleteAll();
        wifiRepository.deleteAll();
        appUsageRepository.deleteAll();
        takenPictureRepository.deleteAll();

        imageFileRepository.deleteAll();
        return new ResponseEntity(HttpStatusCode.valueOf(203));
    }

}
