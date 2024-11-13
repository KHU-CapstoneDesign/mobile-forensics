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
        return UserData.builder()
                .latitude(latitude)
                .longitude(longitude)
                .dateTime(dateTime).build();
    }
}
