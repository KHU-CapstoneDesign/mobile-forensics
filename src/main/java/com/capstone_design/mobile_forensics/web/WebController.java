package com.capstone_design.mobile_forensics.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class WebController {

    @Autowired
    private WebService webService;

//    @GetMapping("/start")
//    public StartForensics()
//    {
//
//    }

    // 사용자 정보 (날짜, 위치, 시간 등) 전송
    // 위치 정보는 위도/경도로 변환되어 전달되어야 함
    // 전달 시, 배치파일 다운로드하도록 연결 (static .bat file )
    // 받은 사용자 정보는 따로 저장한다.
    @PostMapping("/request")
    public ResponseEntity<String> getData(@RequestBody LocalDateTime dateTime,
                                          @RequestBody String Latitude,
                                          @RequestBody String Longitude) {
        String savedUser = webService.saveUser(dateTime, Latitude, Longitude);
        return new ResponseEntity<>(savedUser, HttpStatusCode.valueOf(200));
    }

    // 사용자 정보가 따로 반환되었으니, 프론트에서는 유저 아이디를 쿠키에 저장한다.
    // 배치파일로부터 파일 전송 받는 즉시 프론트에 알림
}
