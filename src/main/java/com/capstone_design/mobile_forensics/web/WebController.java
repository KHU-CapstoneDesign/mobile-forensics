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
    @PostMapping("/result")
    public ResponseEntity<UserData> getData(@RequestBody UserDTO request) {
        ResponseEntity<UserData> response = webService.saveUser(request);
        return response;
    }

    // 저장된 이후에, 프론트엔드에서 따로 다시 GET 요청,
    // 요청 정보에 처음 POST요청에서 응답으로 받았던 유저 아이디를 함께 보내주면
    // 해당 유저 아이디에 대한 정보를 기반으로 데이터 모두 전송
    @GetMapping("/result")
    public ResponseEntity<String> getAllData() {
        return null;
    }
}
