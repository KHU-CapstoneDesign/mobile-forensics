package com.capstone_design.mobile_forensics.log.api;

import com.capstone_design.mobile_forensics.log.dto.WifiDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class GeolocationService {
    @Value("${google.geolocation.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    private GeolocationRequest convertDtoToRequest(WifiDTO dto) {
        return new GeolocationRequest(dto.getBssid(), dto.getSignalStrength(), dto.getDurationMillis(), dto.getMChannelInfo());
    }

    public WifiDTO getLocation(WifiDTO dto) {
        GeolocationRequest request = convertDtoToRequest(dto);

        String url = "https://www.googleapis.com/geolocation/v1/geolocate?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeolocationRequest> entity = new HttpEntity<>(request, headers);
        log.info("request message = {}", entity);
        ResponseEntity<GeolocationResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, GeolocationResponse.class);

        // 응답으로 받은 위도, 경도 정보 추출
        GeolocationResponse geolocationResponse = response.getBody();
        String lat = String.valueOf(geolocationResponse.getLocation().getLat());
        String lng = String.valueOf(geolocationResponse.getLocation().getLng());

        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lng);

        dto.setLatitude(latitude);
        dto.setLongitude(longitude);

        log.info("DTO info = {}", dto );
        return dto;
    }
}
