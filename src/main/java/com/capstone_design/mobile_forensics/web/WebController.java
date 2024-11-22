package com.capstone_design.mobile_forensics.web;


import com.capstone_design.mobile_forensics.file.api.SafeSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class WebController {

    @Autowired
    private WebService webService;
    @Autowired
    private SafeSearchService safeSearchService;

    // 사용자 정보 (날짜, 위치, 시간 등) 전송
    // 위치 정보는 위도/경도로 변환되어 전달되어야 함
    // 받은 사용자 정보는 따로 저장한다.
    @PostMapping("/result")
    public ResponseEntity getData(@RequestBody UserDTO request) {
        ResponseEntity response = webService.saveUser(request);
        return response;
    }

    // 전체 결과보기 (유저 아이디 포함되어야 관련 데이터 조회 가능)
    @GetMapping("/result/user")
    public ResponseEntity countAllData(@CookieValue(name = "userId") Long userId) {
        ResponseEntity response = webService.countAllData(userId);
        return response;
    }

    // 사진 발견 내역 - image 중 camera, trash 타입 가져오기
    @GetMapping("/result/image")
    public ResponseEntity getCameraImage(@CookieValue(name = "userId") Long userId) throws IOException {
        webService.getCameraImages(userId);
        return null;

    }
    // 클라우드 업로드 흔적 발견 내역 - image 중 drive 타입 가져오기
    @GetMapping("/result/cloud")
    public ResponseEntity getCloudImage(@CookieValue(name = "userId") Long userId) throws IOException {
        webService.getCloudImages(userId);
        return null;

    }
    // 사진 삭제 흔적 발견 내역 - image 중 soda 타입 가져오기
    @GetMapping("/result/cache-image")
    public ResponseEntity getCacheImage(@CookieValue(name = "userId") Long userId) throws IOException {
        webService.getCacheImages(userId);
        return null;

    }



    // 클라우드 앱 사용기록 발견 내역 - 네이버 MYBOX 앱 사용기록
    @GetMapping("/result/app-cloud/naver")
    public ResponseEntity getAppUsage_NaverCloud(@CookieValue(name = "userId") Long userId) {
        webService.getNaverCloud_AppUsage(userId);
        return null;

    }
    // 클라우드 앱 사용기록 발견 내역 - 구글 드라이브 앱 사용기록
    @GetMapping("/result/app-cloud/google")
    public ResponseEntity getAppUsage_GoogleDrive(@CookieValue(name = "userId") Long userId) {
        webService.getGoogleCloud_AppUsage(userId);
        return null;

    }



    // 카메라 앱 사용기록 발견 내역 - 스노우 앱 사용기록
    @GetMapping("result/app-cam/snow")
    public ResponseEntity getAppUsage_Snow(@CookieValue(name = "userId") Long userId) {
        webService.getSnow_AppUsage(userId);
        return null;

    }
    // 카메라 앱 사용기록 발견 내역 - 소다 앱 사용기록
    @GetMapping("result/app-cam/soda")
    public ResponseEntity getAppUsage_Soda(@CookieValue(name = "userId") Long userId) {
        webService.getSoda_AppUsage(userId);
        return null;

    }


    // 사진 촬영 로그 발견 내역
    @GetMapping("/result/picture-taken")
    public ResponseEntity getPictureTakenLog(@CookieValue(name = "userId") Long userId) {
        webService.getPictureTakenLog(userId);
        return null;

    }
    // 유사 위치 로그 발견 내역
    @GetMapping("/result/location")
    public ResponseEntity getLocationLog(@CookieValue(name = "userId") Long userId) {
        webService.getLocationLog(userId);
        return null;

    }


    @DeleteMapping("/exit")
    public ResponseEntity exitProgram(@CookieValue(name = "userId") Long userId) {
        return webService.exit(userId);
    }


}
