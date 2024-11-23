package com.capstone_design.mobile_forensics.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    String latitude;
    String longitude;
    LocalDateTime dateTime;

    UserData toEntity(){
        String la = latitude.replace("N", "");
        String lo = longitude.replace("E", "");
        double lat = Double.parseDouble(la);
        double lng = Double.parseDouble(lo);

        return UserData.builder()
                .latitude(lat)
                .longitude(lng)
                .dateTime(dateTime).build();
    }
}
